package com.freeing.authcenter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 重写认证管理器的实现，重写校验密码的方法，因为：
 * 并不是所有的登录认证都需要密码认证
 *
 * @author yanggy
 */
@Component
public class MutableDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    /**
     * 重写密码认证，有自己在 UserDetailsService 完成这些校验，不是所有的认证都需要校验密码
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }
}
