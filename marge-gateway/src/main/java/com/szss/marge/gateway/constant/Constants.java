package com.szss.marge.gateway.constant;

/**
 * 常量类
 * 
 * @author XXX
 * @date 2018/7/3
 */
public class Constants {
    /**
     * client_id参数
     */
    public static final String REQUEST_APP_ID_PARAMETER_NAME = "app_id";
    /**
     * sessionKey参数
     */
    public static final String REQUEST_SESSION_KEY_PARAMETER_NAME = "session_key";
    /**
     * 时间戳
     */
    public static final String REQUEST_TIMESTAMP_PARAMETER_NAME = "timestamp";
    /**
     * 签名的摘要算法，可选值为：hmac，md5
     */
    public static final String REQUEST_SIGN_METHOD_PARAMETER_NAME = "sign_method";
    /**
     * API输入参数签名结果
     */
    public static final String REQUEST_SIGN_PARAMETER_NAME = "sign";
    /**
     * 请求方法
     */
    public static final String REQUEST_HTTP_METHOD_PARAMETER_NAME = "http_method";
    /**
     * 请求路径
     */
    public static final String REQUEST_URI_PARAMETER_NAME = "request_uri";
    /**
     * 有效时间误差，默认为10分钟。
     */
    public static final Integer EFFECTIVE_TIME = 10;
}
