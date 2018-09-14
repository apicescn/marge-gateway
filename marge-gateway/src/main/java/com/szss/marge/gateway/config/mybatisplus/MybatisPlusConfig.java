package com.szss.marge.gateway.config.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

/**
 * @author XXX
 * @date 2018/3/12
 */
@Configuration
@MapperScan("com.szss.marge.gateway.dao")
public class MybatisPlusConfig {

    /**
     * 分页插件
     * 
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}