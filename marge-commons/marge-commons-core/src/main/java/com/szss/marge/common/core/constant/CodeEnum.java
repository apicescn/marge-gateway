package com.szss.marge.common.core.constant;

/**
 * rest接口编码，CodeEnum包含编码为通用编码，编码范围从1000~9999
 *
 * @author XXX
 * @date 2018/3/28
 */
public enum CodeEnum implements ICodeEnum {
    /**
     * 操作成功
     */
    SUCCESS(true, "1000", "操作成功"),
    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(false, "1001", "数据不存在"),
    /**
     * 数据删除失败
     */
    DELETE_FAILED(false, "1002", "数据删除失败"),
    /**
     * 数据新增失败
     */
    INSERT_FAILED(false, "1003", "数据新增失败"),
    /**
     * 数据更新失败
     */
    UPDATE_FAILED(false, "1004", "数据更新失败"),
    /**
     * 接口访问超时
     */
    SOCKET_TIMEOUT(false, "1005", "接口访问超时"),
    /**
     * 加密异常
     */
    ENCRYPT_ERROR(false, "1101", "加密失败"),
    /**
     * 解密异常
     */
    DECRYPT_ERROR(false, "1102", "解密失败"),
    /**
     * 参数异常
     */
    PARAMS_ERROR(false, "1999", "参数异常"),
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(false, "2000", "服务器内部错误");

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
    CodeEnum(Boolean success, String code, String message) {
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
