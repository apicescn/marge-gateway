package com.szss.marge.common.core.model.dto;

import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.constant.ICodeEnum;
import com.szss.marge.common.core.model.dto.RestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用VO（不带分页）
 *
 * @param <T> 实际数据类型
 * @author XXX
 * @date 2018/05/29
 */
@Data
@NoArgsConstructor
public class SimpleDTO<T> extends RestDTO {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7281269010610224891L;

    /**
     * 构造器
     *
     * @param codeEnum code
     */
    public SimpleDTO(ICodeEnum codeEnum) {
        super(codeEnum);
        this.setData(null);
    }
    

    /**
     * 构造器
     *
     * @param data 返回的数据
     */
    public SimpleDTO(T data) {
        super(CodeEnum.SUCCESS);
        this.setData(data);
    }

    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的响应数据", required = true)
    T data;
}
