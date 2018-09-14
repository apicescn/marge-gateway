package com.szss.marge.gateway.rest.impl;// package com.szss.marge.gateway.rest.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szss.marge.auth.client.resource.model.client.ClientDetailsDTO;
import com.szss.marge.common.core.constant.ICodeEnum;
import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.config.feign.FeignClientDetails;
import com.szss.marge.gateway.dao.AppDetailDAO;
import com.szss.marge.gateway.manager.oauth.OauthServerClient;
import com.szss.marge.gateway.model.domain.AppDetailDO;
import com.szss.marge.gateway.model.dto.AppDetailDTO;
import com.szss.marge.gateway.model.dto.TokenDTO;
import com.szss.marge.gateway.model.param.AppDetailParam;
import com.szss.marge.gateway.model.param.ListAppDetailParam;
import com.szss.marge.gateway.rest.AppDetailController;
import com.szss.marge.gateway.rest.constant.RestConstant;
import com.szss.marge.gateway.service.AppDetailService;
import com.szss.marge.gateway.utils.AppDetailUtils;
import com.szss.marge.gateway.utils.Tools;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.szss.marge.common.core.constant.CodeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XXX
 * @date 2018/7/4
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AppDetailControllerImpl.class)
@TestPropertySource(properties = {"spring.profiles.active: test", "flyway.enabled: false"})
public class AppDetailControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OauthServerClient oauthClient;

    @MockBean
    private FeignClientDetails feignClientDetails;
    /**
     * mock appDetailDAO
     */
    @MockBean
    private AppDetailDAO appDetailDAO;

    /**
     * mock appDetailService
     */
    @MockBean
    private AppDetailService appDetailService;

    /**
     * 测试根据appId和appSecret刷新token
     *
     * @throws Exception 异常
     */
    @Test
    public void refreshTokenTest() throws Exception {
        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        String clientId = appDetailParam.getAppId();
        String clientSecret = appDetailParam.getAppSecret();
        String auth = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + Base64.encodeBase64String(auth.getBytes());

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibWFyZ2UtYXV0aC1zZXJ2ZXIiLCJtYXJnZS1hZG1pbi1zZXJ2ZXIiLCJtYXJnZS1hZG1pbiJdLCJleHAiOjE1MjgxNzg5MzgsInVzZXJfbmFtZSI6ImFkbWluIiwianRpIjoiYjQwZDU0NzQtODQwOS00Mjc1LTk1MjAtMGM4NGQyMTg1ZGEwIiwiY2xpZW50X2lkIjoidGVzdCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.g_i1oV_GNusktofqBOoYnherDXuOWs-G1rh72NuaN0U");
        tokenDTO.setExpiresIn(99999L);
        tokenDTO.setJti("b40d5474-8409-4275-9520-0c84d2185da0");
        tokenDTO.setScope("read write");
        tokenDTO.setTokenType("bearer");

        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();

        when(oauthClient.token(basicAuth, OauthServerClient.GRANT_TYPE_CLIENT)).thenReturn(tokenDTO);

        String token = tokenDTO.getAccessToken();
        appDetailParam.setToken(token);
        appDetailParam.setExpiresIn(tokenDTO.getExpiresIn());

        when(appDetailService.update(appDetailParam)).thenReturn(true);

        this.mockMvc
            .perform(post(AppDetailController.REFRESH_TOKEN_APPDETAIL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试根据appId和appSecret刷新token,数据更新失败
     *
     * @throws Exception 异常
     */
    @Test
    public void refreshTokenFailedTest() throws Exception {
        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        String clientId = appDetailParam.getAppId();
        String clientSecret = appDetailParam.getAppSecret();
        String auth = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + Base64.encodeBase64String(auth.getBytes());

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibWFyZ2UtYXV0aC1zZXJ2ZXIiLCJtYXJnZS1hZG1pbi1zZXJ2ZXIiLCJtYXJnZS1hZG1pbiJdLCJleHAiOjE1MjgxNzg5MzgsInVzZXJfbmFtZSI6ImFkbWluIiwianRpIjoiYjQwZDU0NzQtODQwOS00Mjc1LTk1MjAtMGM4NGQyMTg1ZGEwIiwiY2xpZW50X2lkIjoidGVzdCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.g_i1oV_GNusktofqBOoYnherDXuOWs-G1rh72NuaN0U");
        tokenDTO.setExpiresIn(99999L);
        tokenDTO.setJti("b40d5474-8409-4275-9520-0c84d2185da0");
        tokenDTO.setScope("read write");
        tokenDTO.setTokenType("bearer");

        RestDTO restDTO = new RestDTO(CodeEnum.UPDATE_FAILED);

        ObjectMapper objectMapper = new ObjectMapper();

        when(oauthClient.token(basicAuth, OauthServerClient.GRANT_TYPE_CLIENT)).thenReturn(tokenDTO);

        String token = tokenDTO.getAccessToken();
        appDetailParam.setToken(token);
        appDetailParam.setExpiresIn(tokenDTO.getExpiresIn());

        when(appDetailService.update(appDetailParam)).thenReturn(false);

        this.mockMvc
            .perform(post(AppDetailController.REFRESH_TOKEN_APPDETAIL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));

    }

    /**
     * 测试根据appId和appSecret刷新token,抛出异常
     *
     * @throws Exception 异常
     */
    @Test
    public void refreshTokenThrowExceptionTest() throws Exception {
        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        String clientId = appDetailParam.getAppId();
        String clientSecret = appDetailParam.getAppSecret();
        String auth = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + Base64.encodeBase64String(auth.getBytes());

        RestDTO restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);

        ObjectMapper objectMapper = new ObjectMapper();

        when(oauthClient.token(basicAuth, OauthServerClient.GRANT_TYPE_CLIENT))
            .thenThrow(new RuntimeException("假装出了错"));

        this.mockMvc
            .perform(post(AppDetailController.REFRESH_TOKEN_APPDETAIL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));

    }

    /**
     * 测试根据appId和appSecret刷新sessionKey
     *
     * @throws Exception 异常
     */
    @Test
    public void refreshSessionKeyTest() throws Exception {
        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();

        when(appDetailService.updateSessionKey(appDetailParam)).thenReturn(true);

        this.mockMvc
            .perform(post(AppDetailController.REFRESH_SESSIONKEY_APPDETAIL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试根据appId和appSecret刷新sessionKey,更新失败
     *
     * @throws Exception 异常
     */
    @Test
    public void refreshSessionKeyFailedTest() throws Exception {
        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        RestDTO restDTO = new RestDTO(CodeEnum.UPDATE_FAILED);

        ObjectMapper objectMapper = new ObjectMapper();

        when(appDetailService.updateSessionKey(appDetailParam)).thenReturn(false);

        this.mockMvc
            .perform(post(AppDetailController.REFRESH_SESSIONKEY_APPDETAIL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试根据appId和appSecret刷新sessionKey,更新失败
     *
     * @throws Exception 异常
     */
    @Test
    public void refreshSessionKeyThrowExceptionTest() throws Exception {
        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        RestDTO restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);

        ObjectMapper objectMapper = new ObjectMapper();

        when(appDetailService.updateSessionKey(appDetailParam)).thenThrow(new RuntimeException("假装出了错"));

        this.mockMvc
            .perform(post(AppDetailController.REFRESH_SESSIONKEY_APPDETAIL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试根据appId删除appDetail
     *
     * @throws Exception 异常
     */
    @Test
    public void deleteAppDetailTest() throws Exception {

        String appId = "test1";

        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();

        when(appDetailService.deleteById(appId)).thenReturn(true);

        this.mockMvc
            .perform(post(AppDetailController.DELETE_APPDETAIL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("appId", appId))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试根据appId和appSecret更新appDetail
     *
     * @throws Exception 异常
     */
    @Test
    public void updateAppDetailTest() throws Exception {

        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();

        when(appDetailService.update(appDetailParam)).thenReturn(true);

        this.mockMvc
            .perform(post(AppDetailController.UPDATE_APPDETAIL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试根据appId和appSecret新增appDetail
     *
     * @throws Exception 异常
     */
    @Test
    public void insertAppDetailTest() throws Exception {

        AppDetailParam appDetailParam = new AppDetailParam();
        appDetailParam.setAppId("test1");
        appDetailParam.setAppSecret("test111");

        RestDTO restDTO = new RestDTO(CodeEnum.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();

        when(appDetailService.insert(appDetailParam)).thenReturn(true);

        this.mockMvc
            .perform(post(AppDetailController.INSERT_APPDETAIL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(Tools.object2MultiValueMap(appDetailParam)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restDTO)));
    }

    /**
     * 测试appDetail分页查询
     *
     * @throws Exception 异常
     */
    @Test
    public void listAppDetailByPageTest() throws Exception {

        ListAppDetailParam listAppDetailParam = new ListAppDetailParam();
        listAppDetailParam.setPageIndex(RestConstant.DEFAULT_PAGE_INDEX);
        listAppDetailParam.setPageSize(RestConstant.DEFAULT_PAGE_SIZE);

        Page<AppDetailDO> page = new Page<>(RestConstant.DEFAULT_PAGE_INDEX, RestConstant.DEFAULT_PAGE_SIZE);
        List<AppDetailDO> appDetailDOList = AppDetailUtils.fakerAppDetailDOList(RestConstant.DEFAULT_PAGE_SIZE);
        page.setRecords(appDetailDOList);
        page.setTotal(RestConstant.DEFAULT_PAGE_SIZE);

        when(appDetailService.selectListPage(listAppDetailParam)).thenReturn(page);

        SimplePageDTO<List<AppDetailDTO>> simplePageDTO = AppDetailUtils.fakerListAppDetailDTO(appDetailDOList);

        String expectResult = JSON.toJSONStringWithDateFormat(simplePageDTO, "yyyy-MM-dd HH:mm:ss");

        mockMvc.perform(get(AppDetailController.GET_APPDETAIL_LIST, listAppDetailParam)).andDo(print())
            .andExpect(status().isOk()).andExpect(content().json(expectResult));
    }

    /**
     * 测试appDetail分页查询
     *
     * @throws Exception 异常
     */
    @Test
    public void getAppDetailByAppIdTest() throws Exception {

        String appId = "test1";
        AppDetailDO appDetailDO = AppDetailUtils.fakerAppDetailDO(appId);

        when(appDetailService.selectById(appId)).thenReturn(appDetailDO);

        SimpleDTO<AppDetailDTO> simpleDTO = AppDetailUtils.fakerGetAppDetailDTO(appDetailDO);
        String expectResult = JSON.toJSONStringWithDateFormat(simpleDTO, "yyyy-MM-dd HH:mm:ss");

        mockMvc.perform(get("/api/appDetail/" + appId)).andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(expectResult));
    }

    /**
     * 测试获取auth库中client相关信息
     *
     * @throws Exception 异常
     */
    @Test
    public void getClientDetailsTest() throws Exception {
        List<ClientDetailsDTO> clientDetailsDTOList = new ArrayList<>();
        ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO();
        clientDetailsDTO.setClientId("test1");
        clientDetailsDTO.setClientSecret("test1");
        SimpleDTO<List<ClientDetailsDTO>> simpleDTO = new SimpleDTO<>(clientDetailsDTOList);

        ObjectMapper objectMapper = new ObjectMapper();

        when(feignClientDetails.getClientDetails()).thenReturn(simpleDTO);

        this.mockMvc.perform(get(AppDetailController.GET_APPDETAIL_CLIENTS)).andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(simpleDTO)));
    }

}
