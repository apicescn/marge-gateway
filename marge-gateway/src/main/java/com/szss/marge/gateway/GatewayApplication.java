package com.szss.marge.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

/**
 * @author XXX
 * @date 2018/4/26
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableMethodCache(basePackages = "com.szss.marge.gateway.service")
@EnableCreateCacheAnnotation
public class GatewayApplication {
    /**
     * 主方法
     *
     * @param args spring boot参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
