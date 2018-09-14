package com.szss.marge.gateway.manager.oauth;

import com.szss.marge.gateway.model.dto.TokenDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author XXX
 * @date 2018/4/3
 */
@FeignClient(value = "marge-auth-server")
public interface OauthServerClient {

    /**
     * 客户端授权方式
     */
    String GRANT_TYPE_CLIENT = "client_credentials";

    /**
     * 从marge-auth-server获取token
     *
     * @param basicAuth clientId和clientSecret的base64加密
     * @param grantType 授权方式client_credentials
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uaa/oauth/token",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    TokenDTO token(@RequestHeader("Authorization") String basicAuth, @RequestParam("grant_type") String grantType);

}