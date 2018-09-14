package com.szss.marge.common.config.feign;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import feign.RequestTemplate;

/**
 * @author XXX
 * @date 2018/5/10
 */
public class PlusOAuth2FeignRequestInterceptor extends OAuth2FeignRequestInterceptor {
    /**
     * 请求头
     */
    private static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * 构造函数
     * 
     * @param oAuth2ClientContext
     * @param resource
     */
    public PlusOAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
        OAuth2ProtectedResourceDetails resource) {
        super(oAuth2ClientContext, resource);
    }

    /**
     * 如果feign请求已经有请求头Authorization，就不进行添加
     * 
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        if (!template.headers().containsKey(HEADER_AUTHORIZATION)) {
            super.apply(template);
        }
    }
}
