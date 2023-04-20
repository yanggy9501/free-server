package com.freeing.authcenter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * 容器中存在两个 TokenService，byName 注入
     */
    @Autowired
    @Qualifier("jwtTokenService")
    private AuthorizationServerTokenServices jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    /**
     * 客服端详情:认证服务器能够客户端颁发token，这些客服端必然先在该服务备案，客服端详情存储可以基于内存存储和db存储
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 客户端详情服务 - 获取客服端详情
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
        throws Exception {
        clients.withClientDetails(clientDetails());
    }


    /**
     * 令牌端点的访问配置，令牌的发版，校验等等接口
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
            // 认证管理器，密码模式需要
            .authenticationManager(authenticationManager)
            // 令牌管理服务
            .tokenServices(jwtTokenService)
            .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌端点的安全配置
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
            // oauth/token_key是公开
            .tokenKeyAccess("permitAll()")
            // oauth/check_token公开
            .checkTokenAccess("permitAll()")
            // 表单认证（申请令牌）
            .allowFormAuthenticationForClients();
    }
}
