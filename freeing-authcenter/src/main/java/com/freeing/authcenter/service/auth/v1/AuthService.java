package com.freeing.authcenter.service.auth.v1;

import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.domain.entity.GenericUser;

/**
 * 认证接口
 *
 * @author yanggy
 */
public interface AuthService {

    default boolean match(String authType) {
        return false;
    }

    GenericUser execute(AuthParamDto authParamDto);
}
