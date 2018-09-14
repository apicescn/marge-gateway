package com.szss.marge.gateway.config.feign;

import java.util.List;
import com.szss.marge.auth.client.resource.ClientDetailsResource;
import com.szss.marge.auth.client.resource.model.client.ClientDetailsDTO;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.stereotype.Component;
import feign.Client;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author XXX
 * @date 2018/7/23
 */
@Component
public class FeignClientDetails {

    /**
     * client配置
     */
    @Autowired
    private Client client;

    /**
     * interceptor配置
     */
    @Autowired
    private OAuth2FeignRequestInterceptor interceptor;

    /**
     * 注入json解码器
     */
    @Autowired
    @Qualifier("jacksonDecoder")
    private JacksonDecoder jacksonDecoder;

    /**
     * 注入json编码器
     */
    @Autowired
    @Qualifier("jacksonEncoder")
    private JacksonEncoder jacksonEncoder;

    /**
     * oauthServiceId
     */
    @Value("${oauth.application.name}")
    private String oauthServiceId;

    /**
     * oauthContextPath
     */
    @Value("${oauth.context-path}")
    private String oauthContextPath;

    /**
     * 查询client_details信息
     *
     * @return SimplePageDTO
     */
    public SimpleDTO<List<ClientDetailsDTO>> getClientDetails() {

        String url = "http://" + oauthServiceId.toLowerCase() + oauthContextPath;

        ClientDetailsResource clientDetailsResource = Feign.builder().client(client).encoder(jacksonEncoder)
            .decoder(jacksonDecoder).requestInterceptor(interceptor).target(ClientDetailsResource.class, url);
        return clientDetailsResource.getClientDetails();
    }
}
