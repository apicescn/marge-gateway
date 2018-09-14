package com.szss.marge.common.core.model.dto;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.constant.ICodeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用VO（带分页）
 *
 * @param <T> 实际数据类型
 * @author XXX
 * @date 2018/05/29
 */
@Data
@NoArgsConstructor
public class SimplePageDTO<T> extends RestDTO {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5619223391534235352L;

    /**
     * 构造器
     *
     * @param codeEnum code
     * @author 鼠浩安
     * @date 2018/05/29
     */
    public SimplePageDTO(ICodeEnum codeEnum) {
        super(codeEnum);
    }

    /**
     * 构造器
     *
     * @param data 返回的数据
     * @param page 分页信息
     */
    public SimplePageDTO(T data, Pagination page) {
        super(CodeEnum.SUCCESS);
        this.setData(data);
        this.setPageInfo(page);
    }

    /**
     * 统一返回的数据字段
     */
    @ApiModelProperty(value = "返回的响应数据", required = true)
    T data;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", required = true)
    private Integer total;

    /**
     * 当前页的记录数
     */
    @ApiModelProperty(value = "当前页的记录数", required = true)
    private Integer pageSize;

    /**
     * 当前页号
     */
    @ApiModelProperty(value = "当前页号", required = true)
    private Integer pageIndex;

    /**
     * 设置分页信息
     * 
     * @param pageInfo 分页信息
     */
    public void setPageInfo(Pagination pageInfo) {
        this.setTotal(pageInfo.getTotal());
        this.setPageIndex(pageInfo.getCurrent());
        this.setPageSize(pageInfo.getSize());
    }

}
