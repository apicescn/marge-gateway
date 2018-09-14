package com.szss.marge.gateway.model.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/22
 */
@Data
@ApiModel(value = "RatelimitStrategyDTO", description = "RatelimitStrategyDTO")
public class RatelimitStrategyDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1", required = true)
    private Long id;
    /**
     * 策略名称
     */
    @ApiModelProperty(value = "name", example = "策略01", required = true)
    private String name;
    /**
     * 时间单位
     */
    @ApiModelProperty(value = "timeUnit", example = "秒", required = true)
    private String timeUnit;
    /**
     * api调用限制次数
     */
    @ApiModelProperty(value = "apiRatelimitCount", example = "1000", required = true)
    private Integer apiRatelimitCount;
    /**
     * app调用限制次数，appRatelimitCount < apiRatelimitCount
     */
    @ApiModelProperty(value = "appRatelimitCount", example = "100", required = true)
    private Integer appRatelimitCount;

    /**
     * 绑定后台api的id
     */
    @ApiModelProperty(value = "backendApis", example = "api", required = true)
    private List<Long> apiIds;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "dateCreated", example = "2018-07-22 16:14:00", required = true)
    private Date dateCreated;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "lastUpdated", example = "2018-07-22 16:14:00", required = true)
    private Date lastUpdated;
    /**
     * 删除标示
     */
    @ApiModelProperty(value = "deleted", example = "false", required = true)
    private Boolean deleted;
}
