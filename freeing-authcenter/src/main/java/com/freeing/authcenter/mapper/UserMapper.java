package com.freeing.authcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freeing.authcenter.domain.entity.GenericUser;

/**
 * @author yanggy
 */
public interface UserMapper extends BaseMapper<GenericUser> {

    GenericUser selectByUsername(String username);
}
