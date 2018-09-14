package com.szss.marge.common.web.interceptor;

import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.RestDTO;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



/**
 * @author XXX
 * @date 2018/4/13
 */
@ControllerAdvice
public class DefaultExceptionHandler {
    /**
     * 拦截BindException异常
     * 
     * @param e BindException异常
     * @return RestDTO
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RestDTO handle(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder("参数错误：");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getField() + "字段");
            message.append(fieldError.getDefaultMessage() + ";");
        }
        RestDTO restDTO = new RestDTO(CodeEnum.PARAMS_ERROR);
        restDTO.setMessage(message.toString());
        return restDTO;
    }

    /**
     * 拦截ConstraintViolationException异常
     * 
     * @param e ConstraintViolationException异常
     * @return RestDTO
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RestDTO handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        StringBuilder message = new StringBuilder("参数错误：");
        for (ConstraintViolation fieldError : constraintViolationSet) {
            message.append(fieldError.getMessage() + ";");
        }
        RestDTO restDTO = new RestDTO(CodeEnum.PARAMS_ERROR);
        restDTO.setMessage(message.toString());
        return restDTO;
    }

    /**
     * 拦截MissingServletRequestParameterException异常
     *
     * @param e MissingServletRequestParameterException异常
     * @return RestDTO
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RestDTO handle(MissingServletRequestParameterException e) {
        RestDTO restDTO = new RestDTO(CodeEnum.PARAMS_ERROR);
        restDTO.setMessage(e.getMessage());
        return restDTO;
    }

    /**
     * 默认异常 500(默认)
     * 
     * @param e 异常
     * @return Object
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public RestDTO defaultErrorHandler(Exception e) {
        RestDTO restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
        return restDTO;
    }

}
