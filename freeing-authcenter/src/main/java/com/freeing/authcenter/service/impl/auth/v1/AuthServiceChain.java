package com.freeing.authcenter.service.impl.auth.v1;

import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.domain.entity.GenericUser;
import com.freeing.authcenter.service.DefaultUserDetailService;
import com.freeing.authcenter.service.auth.v1.AuthService;
import com.freeing.common.component.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证的过滤器链，必须多例模式，因为认证是存在并发的，并且该类中中存在状态属性
 * 其他方案：采用列表的方式链接每个认证 Service，缺点就是每个 Service 必须设置后继节点，而不能专注于业务。或者注入如下，然后统一设置前驱后继
 *
 * @author yanggy
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthServiceChain {
    private static final Logger logger = LoggerFactory.getLogger(DefaultUserDetailService.class);

    @Autowired
    private List<AuthService> chain;

    private int position;

    public AuthServiceChain(List<AuthService> chain) {
        this.chain = chain;
        position = 0;
    }

    public GenericUser authentication(AuthParamDto authParamDto) {
        while (position >= 0 && position < chain.size()) {
            AuthService authService = chain.get(position++);
            if (authService.match(authParamDto.getAuthType())) {
                return authService.execute(authParamDto);
            }
        }
        return new DefaultAuthServiceImpl().execute(authParamDto);
    }

    /**
     * 兜底方案
     */
    public static class DefaultAuthServiceImpl implements AuthService {
        @Override
        public boolean match(String authType) {
            return true;
        }

        @Override
        public GenericUser execute(AuthParamDto authParamDto) {
            logger.error("错误的认证类型: {}", authParamDto.getAuthType());
            throw new BizException("error.auth.type");
        }
    }
}
