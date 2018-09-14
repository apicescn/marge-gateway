package com.szss.marge.gateway.rest;

import com.szss.marge.auth.client.resource.model.client.ClientDetailsDTO;
import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.model.dto.AppDetailDTO;
import com.szss.marge.gateway.model.param.AppDetailParam;
import com.szss.marge.gateway.model.param.ListAppDetailParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * @author XXX
 * @date 2018/7/5
 */
@Api(tags = {"appDetail-controller"}, description = "AppDetail接口")
public interface AppDetailController {

    /**
     * 根据appId查询appDetail信息url
     */
    String GET_APPDETAIL_BY_APPID = "/api/appDetail/{appId}";

    /**
     * 获取appDetail列表url
     */
    String GET_APPDETAIL_LIST = "/api/appDetail/list";

    /**
     * appDetail数据插入url
     */
    String INSERT_APPDETAIL = "/api/appDetail/insert";

    /**
     * appDetail数据更新url
     */
    String UPDATE_APPDETAIL = "/api/appDetail/update";

    /**
     * 根据appId删除数据url
     */
    String DELETE_APPDETAIL = "/api/appDetail/delete";

    /**
     * appDetail信息生成或者刷新sessionKey url
     */
    String REFRESH_SESSIONKEY_APPDETAIL = "/api/appDetail/refresh/sessionKey";

    /**
     * appDetail信息生成或者刷新token url
     */
    String REFRESH_TOKEN_APPDETAIL = "/api/appDetail/refresh/token";

    /**
     * appDetail信息生成或者刷新token url
     */
    String GET_APPDETAIL_CLIENTS = "/api/appDetail/clients";

    /**
     * 根据appId查询appDetail
     *
     * @param appId appId
     * @return SimpleDTO
     */
    @ApiOperation(value = "根据appId查询appDetail", notes = "根据appId查询appDetail", protocols = "http,https",
        httpMethod = "GET", response = SimpleDTO.class)
    SimpleDTO<AppDetailDTO> getAppDetailByAppId(String appId);

    /**
     * 根据查询条件查询appDetail列表
     *
     * @param listAppDetailParam 查询条件
     * @return AppDetail列表
     */
    @ApiOperation(value = "根据查询条件查询appDetail列表", notes = "根据查询条件查询appDetail列表", protocols = "http,https",
        httpMethod = "POST", response = SimplePageDTO.class)
    SimplePageDTO<List<AppDetailDTO>> listAppDetailByPage(ListAppDetailParam listAppDetailParam);

    /**
     * 添加AppDetail
     *
     * @param appDetailParam AppDetail信息
     * @return 操作结果
     */
    @ApiOperation(value = "添加AppDetail", notes = "添加AppDetail", protocols = "http,https", httpMethod = "POST",
        response = RestDTO.class)
    RestDTO insertAppDetail(AppDetailParam appDetailParam);

    /**
     * 更新appDetail信息
     *
     * @param appDetailParam appDetail信息
     * @return 操作结果
     */
    @ApiOperation(value = "更新appDetail信息", notes = "更新appDetail信息", protocols = "http,https", httpMethod = "POST",
        response = RestDTO.class)
    RestDTO updateAppDetail(AppDetailParam appDetailParam);

    /**
     * 删除appDetail
     *
     * @param appId appId
     * @return 操作结果
     */
    @ApiOperation(value = "删除appDetail", notes = "删除appDetail", protocols = "http,https", httpMethod = "POST",
        response = RestDTO.class)
    RestDTO deleteAppDetail(String appId);

    /**
     * appDetail信息生成或者刷新sessionKey
     *
     * @param appDetailParam appDetail信息
     * @return 操作结果
     */
    @ApiOperation(value = "appDetail信息生成或者刷新sessionKey", notes = "appDetail信息生成或者刷新sessionKey",
        protocols = "http,https", httpMethod = "POST", response = RestDTO.class)
    RestDTO refreshSessionKey(AppDetailParam appDetailParam);

    /**
     * appDetail信息生成或者刷新token
     *
     * @param appDetailParam appDetail信息
     * @return 操作结果
     */
    @ApiOperation(value = "appDetail信息生成或者刷新token", notes = "appDetail信息生成或者刷新token", protocols = "http,https",
        httpMethod = "POST", response = RestDTO.class)
    RestDTO refreshToken(AppDetailParam appDetailParam);

    /**
     * 获取auth库中client相关信息
     *
     * @return SimpleDTO<List<ClientDetailsDTO>>
     */
    @ApiOperation(value = "获取auth库中client相关信息", notes = "获取auth库中client相关信息", protocols = "http,https",
        httpMethod = "POST", response = SimpleDTO.class)
    SimpleDTO<List<ClientDetailsDTO>> getClientDetails();
}
