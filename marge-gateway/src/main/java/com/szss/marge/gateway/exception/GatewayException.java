package com.szss.marge.gateway.exception;

import com.szss.marge.common.core.constant.ICodeEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * @author XXX
 * @date 2018/7/9
 */
@Getter
@Setter
public class GatewayException extends RuntimeException {
    /**
     * 错误编码
     */
    private ICodeEnum codeEnum;

    public GatewayException(ICodeEnum codeEnum) {
        super(codeEnum.message());
        this.codeEnum = codeEnum;
    }

    public GatewayException(String message, ICodeEnum codeEnum) {
        super(message);
        this.codeEnum = codeEnum;
    }

    public GatewayException(String message, Throwable cause, ICodeEnum codeEnum) {
        super(message, cause);
        this.codeEnum = codeEnum;
    }

    public GatewayException(Throwable cause, ICodeEnum codeEnum) {
        super(cause);
        this.codeEnum = codeEnum;
    }

    public GatewayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
        ICodeEnum codeEnum) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.codeEnum = codeEnum;
    }
}
