package com.szss.marge.common.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XXX
 * @date 2018/5/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", example = "100", required = true, position = 2)
    private Integer total;

    /**
     * 当前页的记录数
     */
    @ApiModelProperty(value = "当前页的记录数", example = "100", required = true, position = 3)
    private Integer pageSize;

    /**
     * 当前页号
     */
    @ApiModelProperty(value = "当前页号", example = "1", required = true, position = 4)
    private Integer pageIndex;
    
    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的响应数据", required = true)
    T data;
}
