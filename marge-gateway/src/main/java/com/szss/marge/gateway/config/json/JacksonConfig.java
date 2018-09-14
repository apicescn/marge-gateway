package com.szss.marge.gateway.config.json;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author XXX
 * @date 2018/5/31
 */
@Configuration
public class JacksonConfig {

    /**
     * json 解码
     * 
     * @return
     */
    @Bean(name = "jacksonDecoder")
    @Primary
    public JacksonDecoder jacksonDecoder() {
        return new JacksonDecoder();
    }

    /**
     * json 编码
     * 
     * @return
     */
    @Bean(name = "jacksonEncoder")
    @Primary
    public JacksonEncoder jacksonEncoder() {
        return new JacksonEncoder();
    }
}
