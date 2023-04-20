package com.freeing.authcenterv2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("myAuthorizationServerTokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 客户端详情服务 - 获取客服端详情
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
        throws Exception {
        // 使用in-memory存储
        clients.inMemory()
            // client_id
            .withClient("XcWebApp")
            //客户端密钥
            .secret("XcWebApp")
            //.secret(new BCryptPasswordEncoder().encode("XcWebApp"))//客户端密钥
            // 资源列表
            .resourceIds("xuecheng-plus")
            // 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
            .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
            // 允许的授权范围
            .scopes("all")
            // false跳转到授权页面
            .autoApprove(false)
            // 客户端接收授权码的重定向地址
            .redirectUris("http://www.baidu.com")
        ;
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
            .tokenServices(authorizationServerTokenServices)
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
