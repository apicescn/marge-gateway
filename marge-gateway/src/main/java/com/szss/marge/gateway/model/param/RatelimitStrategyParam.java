package com.szss.marge.gateway.model.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 限流策略参数
 *
 * @author XXX
 * @date 2018/7/22
 */
@Data
public class RatelimitStrategyParam {
    /**
     * id
     */
    @ApiParam(value = "id", example = "1", required = true)
    private Long id;
    /**
     * 策略名称
     */
    @ApiParam(value = "name", example = "策略01", required = true)
    private String name;
    /**
     * 时间单位
     */
    @ApiParam(value = "timeUnit", example = "秒", required = true)
    private String timeUnit;
    /**
     * api调用限制次数
     */
    @ApiParam(value = "apiRatelimitCount", example = "1000", required = true)
    private Integer apiRatelimitCount;
    /**
     * app调用限制次数，appRatelimitCount < apiRatelimitCount
     */
    @ApiParam(value = "appRatelimitCount", example = "100", required = true)
    private Integer appRatelimitCount;
    /**
     * apiIds，多个用“,”连接
     */
    @ApiParam(value = "api,多个用“,”连接", example = "1,2", required = true)
    private String apiIds;
}
