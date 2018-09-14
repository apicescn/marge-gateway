package com.szss.marge.gateway.config.sso;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;

/**
 * sso单点登录配置是否开启
 *
 * @author XXX
 * @date 2017-09-08
 */
@Configuration
@EnableOAuth2Sso
@ConditionalOnProperty(prefix = "sso", name = "enabled")
public class Auth2SsoConfig {

}
