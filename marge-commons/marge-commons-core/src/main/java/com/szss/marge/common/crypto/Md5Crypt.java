package com.szss.marge.common.crypto;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/4/12
 */
@Slf4j
public class Md5Crypt {
    /**
     * 加密串起始位置
     */
    public static final int ENCRYPTION_STRING_START_INDEX = 8;
    /**
     * 加密串结束位置
     */
    public static final int ENCRYPTION_STRING_END_INDEX = 24;

    /**
     * md5的16位加密
     *
     * @param str 需要加密的字符串
     * @return
     */
    public static String encode16(String str) {
        try {
            MessageDigest digist = MessageDigest.getInstance("MD5");
            byte[] rs = digist.digest(str.getBytes("UTF-8"));
            return Hex.encodeHexString(rs).substring(ENCRYPTION_STRING_START_INDEX, ENCRYPTION_STRING_END_INDEX);
        } catch (Exception e) {
            log.error("encode16(str={}) md5的16位加密函数异常 Exception. ErrorMsg:{}", str, e.getMessage(), e);
        }
        return null;

    }

    /**
     * md5的加密
     *
     * @param str 需要加密的字符串
     * @return
     */
    public static String encode(String str) {
        try {
            MessageDigest digist = MessageDigest.getInstance("MD5");
            byte[] rs = digist.digest(str.getBytes("UTF-8"));
            return Hex.encodeHexString(rs);
        } catch (Exception e) {
            log.error("encode(str={}) md5的加密函数异常 Exception. ErrorMsg:{}", str, e.getMessage(), e);
        }
        return null;
    }
}
