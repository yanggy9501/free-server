package com.freeing.authcenter.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * 用户详情
 *
 * @author yanggy
 */
@Service
public class DefaultUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //在内存创建zhangsan,密码123,分配权限p1
        manager.createUser(User.withUsername("zhangsan").password(new BCryptPasswordEncoder().encode("123")).authorities("p1").build());
        //在内存创建lisi,密码456,分配权限p2
        manager.createUser(User.withUsername("lisi").password(new BCryptPasswordEncoder().encode("456")).authorities("p2").build());
        return manager.loadUserByUsername(username);
    }
}
