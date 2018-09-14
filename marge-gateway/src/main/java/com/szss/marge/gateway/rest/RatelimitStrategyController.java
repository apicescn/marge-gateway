package com.szss.marge.gateway.rest;

import java.util.List;

import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.model.dto.RatelimitStrategyDTO;
import com.szss.marge.gateway.model.param.ListRatelimitStrategyParam;
import com.szss.marge.gateway.model.param.RatelimitStrategyParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author XXX
 * @date 2018/7/5
 */
@Api(tags = {"ratelimitStrategy-controller"}, description = "RatelimitStrategy接口")
public interface RatelimitStrategyController {

    /**
     * 根据Id查询限流策略信息url
     */
    String GET_RATELIMIT_STRATEGY_BY_APPID = "/api/ratelimitStrategy/{id}";

    /**
     * 获取限流策略列表url
     */
    String GET_RATELIMIT_STRATEGY_LIST = "/api/ratelimitStrategy/list";

    /**
     * 限流策略数据插入url
     */
    String INSERT_RATELIMIT_STRATEGY = "/api/ratelimitStrategy/insert";

    /**
     * 限流策略数据插入url
     */
    String UPDATE_RATELIMIT_STRATEGY = "/api/ratelimitStrategy/update";

    /**
     * 限流策略数据删除url
     */
    String DELETE_RATELIMIT_STRATEGY = "/api/ratelimitStrategy/delete";

    /**
     * 根据查询条件查询限流策略列表
     *
     * @param ratelimitStrategyParam 查询条件
     * @return SimplePageDTO
     */
    @ApiOperation(value = "根据查询条件查询限流策略列表", notes = "根据查询条件查询限流策略列表", protocols = "http,https", httpMethod = "GET",
        response = SimplePageDTO.class)
    SimplePageDTO<List<RatelimitStrategyDTO>> listRatelimitStrategyByPage(@ApiParam(name = "ratelimitStrategyParam",
        value = "查询条件", required = true) ListRatelimitStrategyParam ratelimitStrategyParam);

    /**
     * 根据限流策略ID查询限流策略
     *
     * @param ratelimitStrategyId 限流策略ID
     * @return SimpleDTO
     */
    @ApiOperation(value = "根据限流策略ID查询限流策略", notes = "根据限流策略ID查询限流策略", protocols = "http,https", httpMethod = "GET",
        response = SimpleDTO.class)
    SimpleDTO<RatelimitStrategyDTO> getRatelimitStrategyById(Long ratelimitStrategyId);

    /**
     * 添加限流策略
     *
     * @param ratelimitStrategyParam 限流策略信息
     * @return 操作结果
     */
    @ApiOperation(value = "添加限流策略", notes = "添加限流策略", protocols = "http,https", httpMethod = "POST",
        consumes = "application/x-www-form-urlencoded", response = RestDTO.class)
    RestDTO insertRatelimitStrategy(RatelimitStrategyParam ratelimitStrategyParam);

    /**
     * 更新限流策略
     *
     * @param ratelimitStrategyParam 限流策略信息
     * @return 操作结果
     */
    @ApiOperation(value = "更新限流策略", notes = "更新限流策略", protocols = "http,https", httpMethod = "POST",
        consumes = "application/x-www-form-urlencoded", response = RestDTO.class)
    RestDTO updateRatelimitStrategy(RatelimitStrategyParam ratelimitStrategyParam);

    /**
     * 删除限流策略
     *
     * @param ratelimitStrategyId 限流策略Id
     * @return 操作结果
     */
    @ApiOperation(value = "删除限流策略", notes = "删除限流策略", protocols = "http,https", httpMethod = "POST",
        response = RestDTO.class)
    RestDTO deleteRatelimitStrategy(Long ratelimitStrategyId);
}
