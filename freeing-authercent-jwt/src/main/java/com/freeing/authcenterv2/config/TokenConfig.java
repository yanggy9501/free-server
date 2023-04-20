package com.freeing.authcenterv2.config;

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
    /**
     * 使用JWT方式生成令牌
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
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
     * 【注意】：系统会自动注入一个 DefaultTokenServices，容器会出现两个 TokenServices，在注入时会出现异常
     * 解决：
     * 1. 采用 @Primary，指出其注入的优先级
     * 2. byName 注入
     *
     * @return
     */
    @Bean(name = "jwtTokenService")
    // @Primary
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        // 支持刷新令牌
        service.setSupportRefreshToken(true);
        // 令牌存储策略
        service.setTokenStore(tokenStore());
        // Jwt 的 TokenEnhancer
         TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
         tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new JwtTokenEnhancer2(), jwtAccessTokenConverter()));
        service.setTokenEnhancer(tokenEnhancerChain);

        // 令牌默认有效期2小时
        service.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(259200);
        return service;
    }

    /**
     * Jwt token 增强，扩展信息
     * 【注意】：这里不能继承 JwtAccessTokenConverter 否则 JwtTokenStore 会多次创建多次，造成最终的Jwt的工具类不是自己的
     * 出现资源服务器无法校验（两边的密钥不一样了）
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
