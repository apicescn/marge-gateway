package com.szss.marge.gateway.config.feign;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import com.szss.marge.common.config.feign.PlusOAuth2FeignRequestInterceptor;

import feign.Logger;

/**
 * feign配置
 *
 * @author XXX
 * @date 2018/4/3
 */
@EnableFeignClients(basePackages = "com.szss.marge.gateway.manager")
@Configuration
@Import(FeignClientsConfiguration.class)
public class FeignConfig {

    /**
     * feign输出日志
     * 
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * oauth2拦截器
     *
     * @param oAuth2ProtectedResourceDetails oauth2配置
     * @return
     */
    @Bean
    OAuth2FeignRequestInterceptor
        oauth2FeignRequestInterceptor(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
        return new PlusOAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), oAuth2ProtectedResourceDetails);
    }

    /**
     * initFeignClientDetails
     *
     * @return FeignClientDetails
     */
    @Bean
    FeignClientDetails feignClientDetails() {
        return new FeignClientDetails();
    }
}