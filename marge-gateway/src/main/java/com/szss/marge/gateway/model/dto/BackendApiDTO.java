package com.szss.marge.gateway.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/22
 */
@Data
@ApiModel(value = "BackendApiDTO", description = "BackendApiDTO")
public class BackendApiDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1", required = true)
    private Long id;
    /**
     * api名称
     */
    @ApiModelProperty(value = "name", example = "api名称", required = true)
    private String name;
    /**
     * 服务ID
     */
    @ApiModelProperty(value = "serviceId", example = "serviceId", required = true)
    private String serviceId;
    /**
     * 路径(使用模式匹配)，例如/foo/*
     */
    @ApiModelProperty(value = "path", example = "path", required = true)
    private String path;
    /**
     * 是否使用参数签名
     */
    @ApiModelProperty(value = "signature", example = "signature", required = true)
    private Boolean signature;
    /**
     * 是否使用token
     */
    @ApiModelProperty(value = "token", example = "token", required = true)
    private Boolean token;
}
