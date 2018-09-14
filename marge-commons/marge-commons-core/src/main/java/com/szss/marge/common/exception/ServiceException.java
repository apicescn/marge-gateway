package com.szss.marge.common.exception;

import com.szss.marge.common.core.constant.ICodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author XXX
 * @date 2018/4/23
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {
    /**
     * 错误编码
     */
    private ICodeEnum codeEnum;

    public ServiceException(ICodeEnum codeEnum) {
        super(codeEnum.message());
        this.codeEnum = codeEnum;
    }

    public ServiceException(String message, ICodeEnum codeEnum) {
        super(message);
        this.codeEnum = codeEnum;
    }

    public ServiceException(String message, Throwable cause, ICodeEnum codeEnum) {
        super(message, cause);
        this.codeEnum = codeEnum;
    }

    public ServiceException(Throwable cause, ICodeEnum codeEnum) {
        super(cause);
        this.codeEnum = codeEnum;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
        ICodeEnum codeEnum) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.codeEnum = codeEnum;
    }
}
