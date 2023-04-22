package com.freeing.authcenter.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一认证的请求参数
 *
 * @author yanggy
 */
@Data
public class AuthParamDto {

    private String username;

    private String password;

    private String mobilePhone;

    /**
     * 验证码
     */
    private String checkCode;

    /**
     * 验证码key
     */
    private String checkCodeKey;

    /**
     * 认证类型，用户密码、手机号、短信等等
     */
    private String authType;

    /**
     * 附加参数
     */
    private Map<String, Object> payload = new HashMap<>();
}
