package com.freeing.authcenter.service.impl.auth.v2;

import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.domain.entity.GenericUser;
import com.freeing.authcenter.service.auth.v2.AbstractAuthService;
import com.freeing.common.component.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证的过滤器链，必须多例模式，因为认证是存在并发的，并且该类中中存在状态属性
 *
 * @author yanggy
 */
@Service
public class AuthServiceManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceManager.class);

    @Autowired
    private List<AbstractAuthService> chain;

    private AbstractAuthService head;

    public AuthServiceManager(List<AbstractAuthService> chain) {
        this.chain = chain;
        addHead();
        addTail();
        // link
        for (int i = 0; i < chain.size() - 1; i++) {
            AbstractAuthService authService = chain.get(i);
            authService.setNext(chain.get(i + 1));
        }
    }

    private void addHead() {
        HeadAuthServiceImpl headAuthService = new HeadAuthServiceImpl();
        chain.add(0, headAuthService);
    }

    /**
     * 兜底方案
     */
    private void addTail() {
        chain.add(new DefaultAuthServiceImpl());
    }

    public GenericUser authentication(AuthParamDto authParamDto) {
        AbstractAuthService point = head;
        while (point != null) {
            if (point.match(authParamDto.getAuthType())) {
                return point.execute(authParamDto);
            }
            point = point.getNext();
        }
        return null;
    }

    protected static class DefaultAuthServiceImpl extends AbstractAuthService {
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

    protected static class HeadAuthServiceImpl extends AbstractAuthService {
        @Override
        public boolean match(String authType) {
            return false;
        }

        @Override
        public GenericUser execute(AuthParamDto authParamDto) {
            logger.error("错误的认证类型: {}", authParamDto.getAuthType());
            return null;
        }
    }
}
