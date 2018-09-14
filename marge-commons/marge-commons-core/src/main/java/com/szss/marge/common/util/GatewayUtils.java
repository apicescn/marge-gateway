package com.szss.marge.common.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/5/21l 请求参数加密
 */
@Slf4j
public class GatewayUtils {
    /**
     * md5算法
     */
    public static final String MD5 = "md5";
    /**
     * HMAC(Hash Message Authentication Code)，散列消息鉴别码，基于密钥的Hash算法的认证协议
     */
    public static final String HMAC = "hmac";
    /**
     * hmac-sha256算法
     */
    public static final String HMAC_SHA256 = "hmac-sha256";
    /**
     * 8个bit,255
     */
    public static final int EIGHT_BITS = 0xFF;

    /**
     * 针对淘宝、京东、拼多多、和marge-cloud-gateway的加密方式
     *
     * 参考：http://open.taobao.com/doc.htm?docId=101617&docType=1
     *
     * 为了防止API调用过程中被黑客恶意篡改，调用任何一个API都需要携带签名，TOP服务端会根据请求参数，对签名进行验证，签名不合法的请求将会被拒绝。TOP目前支持的签名算法有两种：MD5(sign_method=md5)，HMAC_MD5(sign_method=hmac)，签名大体过程如下：
     * 
     * 对所有API请求参数（包括公共参数和业务参数，但除去sign参数和byte[]类型的参数），根据参数名称的ASCII码表的顺序排序。如：foo:1, bar:2, foo_bar:3,
     * foobar:4排序后的顺序是bar:2, foo:1, foo_bar:3, foobar:4。 将排序好的参数名和参数值拼装在一起，根据上面的示例得到的结果为：bar2foo1foo_bar3foobar4。
     * 把拼装好的字符串采用utf-8编码，使用签名算法对编码后的字节流进行摘要。如果使用MD5算法，则需要在拼装的字符串前后加上app的secret后，再进行摘要，如：md5(secret+bar2foo1foo_bar3foobar4+secret)；如果使用HMAC_MD5算法，则需要用app的secret初始化摘要算法后，再进行摘要，如：hmac_md5(bar2foo1foo_bar3foobar4)。
     * 将摘要得到的字节流结果使用十六进制表示，如：hex(“helloworld”.getBytes(“utf-8”)) = “68656C6C6F776F726C64”
     * 说明：MD5和HMAC_MD5都是128位长度的摘要算法，用16进制表示，一个十六进制的字符能表示4个位，所以签名后的字符串长度固定为32个十六进制字符。
     *
     * @param params 参数
     * @param secret secret
     * @param signMethod signMethod
     * @return sign
     * @exception IOException 加密抛出的IOException
     */
    public static String signRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder query = new StringBuilder();
        if (MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (int i = 0; i < keys.length; ++i) {
            String key = keys[i];
            String value = params.get(key);
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }
        byte[] bytes;
        if (HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else if (HMAC_SHA256.equals(signMethod)) {
            bytes = encryptHMACSHA256(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }
        return byte2hex(bytes);
    }

    /**
     * 针对苏宁，唯品会，移动商城加密参数的排序方式
     *
     * @param params 参数
     * @param secret 密钥
     * @param headWithSecret 是否在前面拼上secret
     * @return
     */
    public static String sort(Map<String, String> params, String secret, Boolean headWithSecret) {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder query = new StringBuilder();
        if (headWithSecret) {
            query.append(secret);
        }
        for (int i = 0; i < keys.length; ++i) {
            String key = keys[i];
            String value = params.get(key);
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }
        query.append(secret);
        return query.toString();
    }

    /**
     * 请求参数加密，苏宁，唯品会，移动商城加密方式
     *
     * @param sourceStr 需要加密的串
     * @param secret 密钥
     * @param signMethod signMethod 加密方法
     * @return sign
     * @exception IOException 加密抛出的IOException
     */
    public static String sign(String sourceStr, String secret, String signMethod) throws IOException {
        byte[] bytes;
        if (HMAC.equals(signMethod)) {
            bytes = encryptHMAC(sourceStr, secret);
        } else if (MD5.equals(signMethod)) {
            bytes = encryptMD5(sourceStr);
        } else {
            bytes = encryptHMACSHA256(sourceStr, secret);
        }
        return byte2hex(bytes);
    }

    /**
     * 参数进行md5加密
     *
     * @param data 加密数据
     * @return 加密后字节数组
     * @exception IOException 加密抛出的IOException
     */
    public static byte[] encryptMD5(String data) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(data.getBytes("UTF-8"));
            return bytes;
        } catch (GeneralSecurityException e) {
            throw new IOException(e.toString());
        }
    }

    /**
     * 参数进行Hmac加密
     *
     * @param data 加密数据
     * @param secret 密钥
     * @return 加密后字节数组
     * @exception IOException 加密抛出的IOException
     */
    private static byte[] encryptHMAC(String data, String secret) throws IOException {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));
            return bytes;
        } catch (GeneralSecurityException e) {
            throw new IOException(e.toString());
        }
    }

    /**
     * 参数进行HmacSHA256加密
     *
     * @param data 加密数据
     * @param secret 密钥
     * @return 加密后字节数组
     * @exception IOException 加密抛出的IOException
     */
    private static byte[] encryptHMACSHA256(String data, String secret) throws IOException {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));
            return bytes;
        } catch (GeneralSecurityException e) {
            throw new IOException(e.toString());
        }
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes 需要编码的字节数组
     * @return
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i] & EIGHT_BITS);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

}
