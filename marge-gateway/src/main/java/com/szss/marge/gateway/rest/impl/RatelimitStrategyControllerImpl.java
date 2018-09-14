package com.szss.marge.gateway.rest.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.model.domain.RatelimitStrategyDO;
import com.szss.marge.gateway.model.dto.RatelimitStrategyDTO;
import com.szss.marge.gateway.model.param.ListRatelimitStrategyParam;
import com.szss.marge.gateway.model.param.RatelimitStrategyParam;
import com.szss.marge.gateway.rest.RatelimitStrategyController;
import com.szss.marge.gateway.service.RatelimitStrategyCacheService;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/7/22
 */
@Slf4j
@RestController
public class RatelimitStrategyControllerImpl implements RatelimitStrategyController {

    /**
     * RatelimitStrategyCacheService
     */
    @Autowired
    private RatelimitStrategyCacheService ratelimitStrategyCacheService;

    /**
     * 根据查询条件查询限流策略列表
     *
     * @param ratelimitStrategyParam 查询条件
     * @return
     */
    @GetMapping(value = GET_RATELIMIT_STRATEGY_LIST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimplePageDTO<List<RatelimitStrategyDTO>>
        listRatelimitStrategyByPage(ListRatelimitStrategyParam ratelimitStrategyParam) {
        SimplePageDTO<List<RatelimitStrategyDTO>> simplePageDTO =
            new SimplePageDTO<List<RatelimitStrategyDTO>>(CodeEnum.SUCCESS);
        try {
            simplePageDTO = ratelimitStrategyCacheService.listRatelimitStrategyByPage(ratelimitStrategyParam);
        } catch (Exception e) {
            log.error(
                "listRatelimitStrategyByPage(ratelimitStrategyParam={}) 分页查询ratelimitStrategy接口异常 Exception. ErrorMsg:{}",
                ratelimitStrategyParam, e.getMessage(), e);
            simplePageDTO = new SimplePageDTO<List<RatelimitStrategyDTO>>(CodeEnum.UNKNOWN_ERROR);

        }
        return simplePageDTO;
    }

    /**
     * 根据限流策略ID查询限流策略信息
     *
     * @param ratelimitStrategyId 限流策略ID
     * @return
     */
    @GetMapping(value = GET_RATELIMIT_STRATEGY_BY_APPID, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimpleDTO<RatelimitStrategyDTO> getRatelimitStrategyById(
        @ApiParam(name = "id", value = "限流策略id", required = true) @PathVariable("id") Long ratelimitStrategyId) {
        try {
            RatelimitStrategyDO ratelimitStrategyDO = ratelimitStrategyCacheService.selectById(ratelimitStrategyId);
            RatelimitStrategyDTO ratelimitStrategyDTO = new RatelimitStrategyDTO();
            BeanUtils.copyProperties(ratelimitStrategyDO, ratelimitStrategyDTO);
            List<Long> listApiId = ratelimitStrategyCacheService.getListApiIdByRatelimitStrategyId(ratelimitStrategyId);
            ratelimitStrategyDTO.setApiIds(listApiId);
            SimpleDTO<RatelimitStrategyDTO> getRatelimitStrategyDTO =
                new SimpleDTO<RatelimitStrategyDTO>(CodeEnum.SUCCESS);
            getRatelimitStrategyDTO.setData(ratelimitStrategyDTO);
            return getRatelimitStrategyDTO;
        } catch (Exception e) {
            log.error("getRatelimitStrategyById(ratelimitStrategyId={}) 查询元素接口异常 Exception. ErrorMsg:{}",
                ratelimitStrategyId, e.getMessage(), e);
            SimpleDTO<RatelimitStrategyDTO> getRatelimitStrategyDTO =
                new SimpleDTO<RatelimitStrategyDTO>(CodeEnum.UNKNOWN_ERROR);
            return getRatelimitStrategyDTO;
        }
    }

    /**
     * 添加限流策略
     *
     * @param ratelimitStrategyParam 限流策略信息
     * @return 操作结果
     */
    @PostMapping(value = INSERT_RATELIMIT_STRATEGY, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO insertRatelimitStrategy(RatelimitStrategyParam ratelimitStrategyParam) {
        return null;
    }

    /**
     * 更新限流策略
     *
     * @param ratelimitStrategyParam 限流策略信息
     * @return 操作结果
     */
    @PostMapping(value = UPDATE_RATELIMIT_STRATEGY, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO updateRatelimitStrategy(RatelimitStrategyParam ratelimitStrategyParam) {
        RestDTO response = new RestDTO(CodeEnum.SUCCESS);
        try {
            ratelimitStrategyCacheService.updateRetelimitStrategy(ratelimitStrategyParam);
        } catch (Exception e) {
            response = new RestDTO(CodeEnum.UNKNOWN_ERROR);
            log.error("updateRatelimitStrategy(ratelimitStrategyParam={}) 添加角色接口异常 Exception. ErrorMsg:{}", ratelimitStrategyParam, e.getMessage(), e);
        }
        return response;
    }

    /**
     * 删除限流策略
     *
     * @param ratelimitStrategyId 限流策略Id
     * @return 操作结果
     */
    @PostMapping(value = DELETE_RATELIMIT_STRATEGY, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO deleteRatelimitStrategy(
        @ApiParam(name = "id", value = "限流策略id", required = true) @PathVariable("id") Long ratelimitStrategyId) {
        return null;
    }
}
