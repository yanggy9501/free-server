package com.freeing.authcenter.service;

import com.alibaba.fastjson.JSON;
import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.service.impl.auth.v1.AuthServiceChain;
import com.freeing.common.component.exception.BizException;
import com.freeing.common.web.utils.I18Utils;
import com.freeing.common.web.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 统一认证的查询用户详情入口，包含：用户名，手机号，验证码等等
 *
 * @author yanggy
 */
@Service
public class DefaultUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserDetailService.class);

    /**
     * 查询用户详情
     *
     * @param authParam 如：username={"username": "name", password"="pwd", "authType":"password"}
     *
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String authParam) throws UsernameNotFoundException {
        AuthParamDto authParamDto = parseToAuthParamsDto(authParam);
        // AuthServiceChain 多例模式，因为其有状态属性，注意必须以这样的方式获取以解决并发问题
        AuthServiceChain authServiceChain = SpringUtils.getBean(AuthServiceChain.class);
        return authServiceChain.authentication(authParamDto);
    }

    private AuthParamDto parseToAuthParamsDto(String authParamsJson) {
        AuthParamDto authParamDto;
        try {
            authParamDto = JSON.parseObject(authParamsJson, AuthParamDto.class);
        } catch (Exception e) {
            logger.error("统一认证的请求参数格式不符合要求：{}", authParamsJson);
            throw new BizException(I18Utils.getMessage("auth.param.parse.error"));
        }
        return authParamDto;
    }
}
