package com.freeing.authcenter.service.auth.v2;

import com.freeing.authcenter.domain.dto.AuthParamDto;
import com.freeing.authcenter.domain.entity.GenericUser;
import lombok.Data;

/**
 * 认证接口
 *
 * @author yanggy
 */
@Data
public abstract class AbstractAuthService {

    private AbstractAuthService next;

    public boolean match(String authType) {
        return false;
    }

    public abstract GenericUser execute(AuthParamDto authParamDto);
}
