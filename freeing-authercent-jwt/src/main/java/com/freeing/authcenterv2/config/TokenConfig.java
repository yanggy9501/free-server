package com.freeing.authcenterv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;

/**
 * Token 配置
 **/
@Configuration
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
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 加密密钥（可以采用属性注入方式生产中建议加密）
        converter.setSigningKey("123");
        return converter;
    }


    /**
     * 令牌服务
     *
     * @return
     */
    @Bean(name="authorizationServerTokenServicesCustom")
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        // 支持刷新令牌
        service.setSupportRefreshToken(true);
        // 令牌存储策略
        service.setTokenStore(tokenStore());
        // 设置增强的 TokenEnhancer 或者
        // TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tulingTokenEnhancer(),jwtAccessTokenConverter()));
        service.setTokenEnhancer(new JwtTokenEnhancer2());

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
    public static class JwtTokenEnhancer2 extends  JwtAccessTokenConverter {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            HashMap<String, Object> extra = new HashMap<>();
            extra.put("lover", "jwt");
            extra.put("sex", "m");
            extra.put("other", "xxx");
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extra);
            return super.enhance(accessToken, authentication);
        }
    }
}
