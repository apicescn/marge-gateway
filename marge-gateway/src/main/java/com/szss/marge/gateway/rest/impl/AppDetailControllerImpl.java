package com.szss.marge.gateway.rest.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.szss.marge.auth.client.resource.model.client.ClientDetailsDTO;
import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.config.feign.FeignClientDetails;
import com.szss.marge.gateway.manager.oauth.OauthServerClient;
import com.szss.marge.gateway.model.domain.AppDetailDO;
import com.szss.marge.gateway.model.dto.AppDetailDTO;
import com.szss.marge.gateway.model.dto.TokenDTO;
import com.szss.marge.gateway.model.param.AppDetailParam;
import com.szss.marge.gateway.model.param.ListAppDetailParam;
import com.szss.marge.gateway.rest.AppDetailController;
import com.szss.marge.gateway.rest.constant.RestConstant;
import com.szss.marge.gateway.service.AppDetailService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XXX
 * @date 2018/7/5
 */
@Slf4j
@RestController
public class AppDetailControllerImpl implements AppDetailController {

    /**
     * appDetailService注入
     */
    @Autowired
    private AppDetailService appDetailService;

    /**
     * oauth客户端接口
     */
    @Autowired
    private OauthServerClient oauthClient;

    /**
     * 注入feignClientDetails
     */
    @Autowired
    private FeignClientDetails feignClientDetails;

    /**
     * 根据appId查询appDetail
     *
     * @param appId appIdString
     * @return SimpleDTO
     */
    @GetMapping(value = GET_APPDETAIL_BY_APPID, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimpleDTO<AppDetailDTO> getAppDetailByAppId(
        @PathVariable("appId") @ApiParam(name = "appId", value = "appId", required = true) String appId) {
        try {
            AppDetailDO appDetailDO = appDetailService.selectById(appId);
            if (appDetailDO == null) {
                SimpleDTO<AppDetailDTO> simpleDTO = new SimpleDTO<AppDetailDTO>(CodeEnum.DATA_NOT_FOUND);
                return simpleDTO;
            }
            AppDetailDTO appDetailDTO = new AppDetailDTO();
            BeanUtils.copyProperties(appDetailDO, appDetailDTO);
            SimpleDTO<AppDetailDTO> simpleDTO = new SimpleDTO<AppDetailDTO>(appDetailDTO);
            return simpleDTO;
        } catch (Exception e) {
            log.error("getAppDetailByAppId(appId={}) 根据appId查询appDetail接口异常 Exception. ErrorMsg:{}", appId,
                e.getMessage(), e);
            SimpleDTO<AppDetailDTO> simpleDTO = new SimpleDTO<AppDetailDTO>(CodeEnum.UNKNOWN_ERROR);
            return simpleDTO;
        }
    }

    /**
     * 根据查询条件查询appDetail列表
     *
     * @param listAppDetailParam 查询条件
     * @return AppDetail列表
     */
    @GetMapping(value = GET_APPDETAIL_LIST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimplePageDTO<List<AppDetailDTO>> listAppDetailByPage(ListAppDetailParam listAppDetailParam) {
        if (listAppDetailParam.getPageIndex() == 0) {
            listAppDetailParam.setPageIndex(RestConstant.DEFAULT_PAGE_INDEX);
        }
        if (listAppDetailParam.getPageSize() == 0) {
            listAppDetailParam.setPageSize(RestConstant.DEFAULT_PAGE_SIZE);
        }

        try {
            Page<AppDetailDO> page = appDetailService.selectListPage(listAppDetailParam);
            SimplePageDTO<List<AppDetailDTO>> simplePageDTO = new SimplePageDTO<List<AppDetailDTO>>(CodeEnum.SUCCESS);
            List<AppDetailDO> appDetailDOList = page.getRecords();
            List<AppDetailDTO> apiDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(appDetailDOList)) {
                appDetailDOList.stream().forEach(u -> {
                    AppDetailDTO apiDTO = new AppDetailDTO();
                    BeanUtils.copyProperties(u, apiDTO);
                    apiDTOList.add(apiDTO);
                });
            }
            simplePageDTO.setData(apiDTOList);
            simplePageDTO.setPageIndex(page.getCurrent());
            simplePageDTO.setPageSize(page.getSize());
            simplePageDTO.setTotal(page.getTotal());
            return simplePageDTO;
        } catch (Exception e) {
            log.error("listAppDetailByPage(listAppDetailParam={}) 分页查询appDetail接口异常 Exception. ErrorMsg:{}",
                listAppDetailParam, e.getMessage(), e);
            SimplePageDTO<List<AppDetailDTO>> simplePageDTO
                = new SimplePageDTO<List<AppDetailDTO>>(CodeEnum.UNKNOWN_ERROR);
            return simplePageDTO;
        }

    }

    /**
     * 添加AppDetail
     *
     * @param appDetailParam AppDetail信息
     * @return 操作结果
     */
    @PostMapping(value = INSERT_APPDETAIL, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO insertAppDetail(@Validated AppDetailParam appDetailParam) {
        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);
        try {
            boolean result = appDetailService.insert(appDetailParam);
            if (!result) {
                restDTO = new RestDTO(CodeEnum.INSERT_FAILED);
            }
        } catch (Exception e) {
            log.error("insertAppDetail(appDetailParam={}) 添加appDetail接口异常 Exception. ErrorMsg:{}", appDetailParam,
                e.getMessage(), e);
            restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
        }
        return restDTO;
    }

    /**
     * 更新appDetail信息
     *
     * @param appDetailParam appDetail信息
     * @return 操作结果
     */
    @PostMapping(value = UPDATE_APPDETAIL, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO updateAppDetail(@Validated AppDetailParam appDetailParam) {
        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);
        try {
            boolean result = appDetailService.update(appDetailParam);
            if (!result) {
                restDTO = new RestDTO(CodeEnum.UPDATE_FAILED);
            }
        } catch (Exception e) {
            log.error("updateAppDetail(appDetailParam={}) 更新appDetail信息接口异常 Exception. ErrorMsg:{}", appDetailParam,
                e.getMessage(), e);
            restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
        }
        return restDTO;
    }

    /**
     * 删除appDetail
     *
     * @param appId appId
     * @return 操作结果
     */
    @PostMapping(value = DELETE_APPDETAIL, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO deleteAppDetail(@ApiParam(name = "appId", value = "appId", required = true) String appId) {
        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);
        try {
            boolean result = appDetailService.deleteById(appId);
            if (!result) {
                restDTO = new RestDTO(CodeEnum.DELETE_FAILED);
            }
        } catch (Exception e) {
            log.error("deleteAppDetail(id={}) 删除appDetail接口异常 Exception. ErrorMsg:{}", appId, e.getMessage(), e);
            restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
        }
        return restDTO;
    }

    /**
     * appDetail信息生成或者刷新sessionKey
     *
     * @param appDetailParam appDetail信息
     * @return 操作结果
     */
    @PostMapping(value = REFRESH_SESSIONKEY_APPDETAIL, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO refreshSessionKey(@Validated AppDetailParam appDetailParam) {
        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);
        try {
            boolean result = appDetailService.updateSessionKey(appDetailParam);
            if (!result) {
                restDTO = new RestDTO(CodeEnum.UPDATE_FAILED);
            }
        } catch (Exception e) {
            log.error("refreshSessionKey(appDetailParam={}) appDetail信息生成或者刷新sessionKey接口异常 Exception. ErrorMsg:{}",
                appDetailParam, e.getMessage(), e);
            restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
        }
        return restDTO;
    }

    /**
     * appDetail信息生成或者刷新token
     *
     * @param appDetailParam appDetail信息
     * @return 操作结果
     */
    @PostMapping(value = REFRESH_TOKEN_APPDETAIL, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public RestDTO refreshToken(@Validated AppDetailParam appDetailParam) {
        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);
        try {
            String clientId = appDetailParam.getAppId();
            String clientSecret = appDetailParam.getAppSecret();
            String auth = clientId + ":" + clientSecret;
            String basicAuth = "Basic " + Base64.encodeBase64String(auth.getBytes());

            TokenDTO tokenDTO = oauthClient.token(basicAuth, OauthServerClient.GRANT_TYPE_CLIENT);
            String token = tokenDTO.getAccessToken();
            appDetailParam.setToken(token);
            appDetailParam.setExpiresIn(tokenDTO.getExpiresIn());
            boolean result = appDetailService.update(appDetailParam);
            if (!result) {
                restDTO = new RestDTO(CodeEnum.UPDATE_FAILED);
            }
        } catch (Exception e) {
            log.error("refreshToken(appDetailParam={}) appDetail信息生成或者刷新token接口异常 Exception. ErrorMsg:{}",
                appDetailParam, e.getMessage(), e);
            restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
        }
        return restDTO;
    }

    /**
     * 获取auth库中client相关信息
     *
     * @return SimpleDTO<List<ClientDetailsDTO>>
     */
    @GetMapping(value = GET_APPDETAIL_CLIENTS, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public SimpleDTO<List<ClientDetailsDTO>> getClientDetails() {
        SimpleDTO<List<ClientDetailsDTO>> simpleDTO;
        try {
            simpleDTO = feignClientDetails.getClientDetails();
        } catch (Exception e) {
            log.error("getClientDetails() 获取auth库中client相关信息接口异常 Exception. ErrorMsg:{}", e.getMessage(), e);
            simpleDTO = new SimpleDTO(CodeEnum.UNKNOWN_ERROR);
        }
        return simpleDTO;
    }

}
