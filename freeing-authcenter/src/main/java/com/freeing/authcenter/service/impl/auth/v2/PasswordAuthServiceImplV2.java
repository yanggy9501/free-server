package com.freeing.authcenter.service.impl.auth.v2;

import com.freeing.authcenter.constants.AuthType;
import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.domain.entity.GenericUser;
import com.freeing.authcenter.service.auth.v2.AbstractAuthService;
import org.springframework.stereotype.Service;

/**
 * @author yanggy
 */
@Service("passwordAuthServiceV2")
public class PasswordAuthServiceImplV2 extends AbstractAuthService {
    @Override
    public boolean match(String authType) {
        return AuthType.PASSWORD == AuthType.getType(authType);
    }

    @Override
    public GenericUser execute(AuthParamDto authParamDto) {
        return null;
    }
}
