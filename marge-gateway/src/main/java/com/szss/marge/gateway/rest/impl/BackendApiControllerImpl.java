package com.szss.marge.gateway.rest.impl;

import com.szss.marge.gateway.model.param.ListBackendApiParam;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.model.domain.BackendApiDO;
import com.szss.marge.gateway.rest.BackendApiController;
import com.szss.marge.gateway.service.BackendApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/7/22
 */
@Slf4j
@RestController
public class BackendApiControllerImpl implements BackendApiController {
    /**
     * BackendApiService
     */
    @Autowired
    private BackendApiService backendApiService;

    /**
     * 获取后台服务api的url
     * 
     * @return
     */
    @GetMapping(value = GET_BACKEND_API_ALL, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimplePageDTO<List<BackendApiDO>> getAll() {
        SimplePageDTO<List<BackendApiDO>> simplePageDTO = new SimplePageDTO<List<BackendApiDO>>(CodeEnum.SUCCESS);
        try {
            List<BackendApiDO> listBackendApiDO = backendApiService.getAll();
            simplePageDTO.setData(listBackendApiDO);
        } catch (Exception e) {
            log.error("getAll 获取后台服务api接口异常 Exception. ErrorMsg:{}", e.getMessage(), e);
            simplePageDTO = new SimplePageDTO<List<BackendApiDO>>(CodeEnum.UNKNOWN_ERROR);
        }
        return simplePageDTO;
    }

    /**
     * 根据限流策略Id查询后台服务api的url
     * 
     * @param listBackendApiParam 查询参数
     * @return
     */
    @GetMapping(value = GET_BACKEND_API_BY_RATELIMIT_STRATEGY_ID, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimplePageDTO<List<BackendApiDO>>
        getListBackendApiDTOByRatelimitStrategyId(ListBackendApiParam listBackendApiParam) {
        SimplePageDTO<List<BackendApiDO>> simplePageDTO = new SimplePageDTO<List<BackendApiDO>>(CodeEnum.SUCCESS);
        try {
            simplePageDTO = backendApiService.getListBackendApiDTOByRatelimitStrategyId(
                listBackendApiParam.getRatelimitStrategyId(), listBackendApiParam.getPageIndex(), listBackendApiParam.getPageSize());
        } catch (Exception e) {
            log.error("getListBackendApiDTOByRatelimitStrategyId({}) 获取后台服务api接口异常 Exception. ErrorMsg:{}",
                listBackendApiParam, e.getMessage(), e);
            simplePageDTO = new SimplePageDTO<List<BackendApiDO>>(CodeEnum.UNKNOWN_ERROR);
        }
        return simplePageDTO;
    }
}
