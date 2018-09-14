package com.szss.marge.common.core.model.dto;

import java.io.Serializable;

import com.szss.marge.common.core.constant.ICodeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XXX
 * @date 2018/3/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestDTO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3462433258560124278L;
    /**
     * 响应代码
     */
    @ApiModelProperty(value = "响应代码", example = "100", required = true, position = 0)
    private String code;

    /**
     * 成功标示
     */
    @ApiModelProperty(value = "是否成功", example = "true", required = true, position = 1)
    private Boolean success;

    /**
     * 响应代码说明
     */
    @ApiModelProperty(value = "消息", example = "接口调用成功", required = true, position = 2)
    private String message;

    /**
     * 构造函数，所有的错误编码枚举需要实现BaseCode接口
     * 
     * @param codeEnum
     */
    public RestDTO(ICodeEnum codeEnum) {
        this.code = codeEnum.code();
        this.success = codeEnum.success();
        this.message = codeEnum.message();
    }
}
