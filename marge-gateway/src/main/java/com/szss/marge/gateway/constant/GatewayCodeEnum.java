package com.szss.marge.gateway.constant;

import com.szss.marge.common.core.constant.ICodeEnum;

/**
 * rest接口编码，CodeEnum包含编码为通用编码，编码范围从1000~9999
 *
 * @author XXX
 * @date 2018/3/28
 */
public enum GatewayCodeEnum implements ICodeEnum {
    /**
     * app id为空
     */
    APP_ID_EMPTY(false, "1200", "App id is empty!"),
    /**
     * app id错误
     */
    APP_ID_ERROR(false, "1201", "App id is error!"),
    /**
     * session key为空
     */
    SESSION_KEY_EMPTY(false, "1202", "Session key is empty!"),
    /**
     * session key错误
     */
    SESSION_KEY_ERROR(false, "1203", "Session key is error!"),
    /**
     * 时间戳为空
     */
    TIMESTAMP_EMPTY(false, "1204", "Timestamp is empty!"),
    /**
     * 请求超时
     */
    REQUEST_URL_TIMEOUT(false, "1205", "This request url is timeout!"),
    /**
     * 时间戳错误
     */
    TIMESTAMP_ERROR(false, "1206", "This timestamp value is not unix timestamp!"),
    /**
     * 签名方法为空
     */
    SIGN_METHOD_EMPTY(false, "1207", "Sign method is empty!"),
    /**
     * 签名方法错误
     */
    SIGN_METHOD_ERROR(false, "1208", "Signature method is error,please choose md5、hmac or hmac-sha256 algorithm!"),
    /**
     * 签名字符串为空
     */
    SIGN_EMPTY(false, "1209", "Sign is empty!"),
    /**
     * 无效的签名
     */
    INVALID_SIGN(false, "1210", "Invalid sign!"),
    /**
     * token 没有找到
     */
    TOKEN_NOT_FOUND(false, "1211", "Token is not found!"),
    /**
     * 超出API限流控制
     */
    BEYOND_API_RATE_LINIT(false, "1213", "Beyoud the api rate limit count!"),
    /**
     * 超出APP限流控制
     */
    BEYOND_APP_RATE_LINIT(false, "1213", "Beyoud the app rate limit count!");

    /**
     * 成功标示
     */
    private Boolean success;
    /**
     * 响应代码
     */
    private String code;
    /**
     * 响应代码说明
     */
    private String message;

    /**
     * 构造方法
     */
    GatewayCodeEnum(Boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    /**
     * 结果
     *
     * @return 操作结果
     */
    @Override
    public Boolean success() {
        return this.success;
    }

    /**
     * 编号
     *
     * @return 操作结果编号
     */
    @Override
    public String code() {
        return this.code;
    }

    /**
     * 说明
     *
     * @return 操作结果说明
     */
    @Override
    public String message() {
        return this.message;
    }
}
