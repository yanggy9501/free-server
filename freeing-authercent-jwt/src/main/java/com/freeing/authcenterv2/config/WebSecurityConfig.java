package com.freeing.authcenterv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Security 配置
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 认证需要
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置用户信息服务
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        //这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //在内存创建zhangsan,密码123,分配权限p1
        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
        //在内存创建lisi,密码456,分配权限p2
        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //密码为明文方式
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 配置安全拦截机制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            // 访问/r开始的请求需要认证通过
            .antMatchers("/r/**").authenticated()
            //其它请求全部放行
            .anyRequest().permitAll()
            .and()
            // 登录成功跳转到/login-success
            .formLogin().successForwardUrl("/login-success");
        // 退出地址
        http.logout().logoutUrl("/logout");
    }
}
