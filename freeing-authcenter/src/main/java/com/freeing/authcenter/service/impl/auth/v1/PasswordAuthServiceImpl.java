package com.freeing.authcenter.service.impl.auth.v1;

import com.freeing.authcenter.constants.AuthType;
import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.domain.entity.GenericUser;
import com.freeing.authcenter.mapper.UserMapper;
import com.freeing.authcenter.service.auth.v1.AuthService;
import com.freeing.common.component.exception.BizException;
import com.freeing.common.web.utils.I18Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 密码认证
 *
 * @author yanggy
 */
@Service("passwordAuthService")
public class PasswordAuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordAuthServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean match(String authType) {
        return AuthType.PASSWORD == AuthType.getType(authType);
    }

    @Override
    public GenericUser execute(AuthParamDto authParamDto) {
        String username = authParamDto.getUsername();
        GenericUser user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BizException("user.not.exist");
        }
        // 校验验证码（因为是统一认证，不适合设置过滤器方式进行校验验证码）

        // 校验密码
        if (!passwordEncoder.matches(authParamDto.getPassword(), user.getPassword())) {
            logger.debug("认证失败: 密码不匹配");
            throw new BadCredentialsException(I18Utils.getMessage("bad.credentials"));
        }
        return user;
    }
}
