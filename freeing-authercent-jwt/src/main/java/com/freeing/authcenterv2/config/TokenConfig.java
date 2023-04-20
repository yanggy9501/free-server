package com.freeing.authcenterv2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Token 配置
 **/
@Configuration(proxyBeanMethods = true)
public class TokenConfig {
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenStore tokenStore;

    /**
     * 使用JWT方式生成令牌
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * 使用同一个密钥来编码 JWT 中的  OAuth2 令牌
     *
     * @return
     */
    @Bean("jwtAccessTokenConverter")
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 加密密钥（可以采用属性注入方式生产中建议加密）
        converter.setSigningKey("123abcd");
        return converter;
    }


    /**
     * 令牌服务
     *
     * @return
     */
    @Bean("myAuthorizationServerTokenServices")
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        // 支持刷新令牌
        service.setSupportRefreshToken(true);
        // 令牌存储策略
        service.setTokenStore(tokenStore);
        // 设置增强的 TokenEnhancer 或者
         TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
         tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new JwtTokenEnhancer2(), jwtAccessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        // 令牌默认有效期2小时
        service.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(259200);
        return service;
    }


    /**
     * Jwt token 增强
     * 扩展信息
     */
    @Component
    public static class JwtTokenEnhancer2 implements TokenEnhancer {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            HashMap<String, Object> extra = new HashMap<>();
            extra.put("lover", "jwt");
            extra.put("sex", "m");
            extra.put("other", "xxx");
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extra);
            return accessToken;
        }
    }
}
