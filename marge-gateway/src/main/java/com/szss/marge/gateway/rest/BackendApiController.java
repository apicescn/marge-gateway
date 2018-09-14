package com.szss.marge.gateway.rest;

import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.model.domain.BackendApiDO;
import com.szss.marge.gateway.model.dto.RatelimitStrategyDTO;
import com.szss.marge.gateway.model.param.ListBackendApiParam;
import com.szss.marge.gateway.model.param.ListRatelimitStrategyParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;

/**
 * @author XXX
 * @date 2018/7/5
 */
@Api(tags = {"backendApi-controller"}, description = "BackendApi接口")
public interface BackendApiController {
    /**
     * 根据限流策略Id查询后台服务api的url
     */
    String GET_BACKEND_API_BY_RATELIMIT_STRATEGY_ID = "/api/backendApi/ratelimitStrategyId";

    /**
     * 获取后台服务api的url
     */
    String GET_BACKEND_API_ALL = "/api/backendApi/all";

    /**
     * 获取后台服务api的url
     * 
     * @return SimplePageDTO
     */
    @ApiOperation(value = "获取后台服务api的url", notes = "获取后台服务api的url", protocols = "http,https", httpMethod = "GET",
        response = SimplePageDTO.class)
    SimplePageDTO<List<BackendApiDO>> getAll();

  /**
   * 根据限流策略Id查询后台服务api的url
   * @param listBackendApiParam 查询参数
   * @return
   */
    @ApiOperation(value = "根据限流策略Id查询后台服务api的url", notes = "根据限流策略Id查询后台服务api的url", protocols = "http,https", httpMethod = "GET",
        response = SimplePageDTO.class)
    SimplePageDTO<List<BackendApiDO>> getListBackendApiDTOByRatelimitStrategyId(ListBackendApiParam listBackendApiParam);
}
