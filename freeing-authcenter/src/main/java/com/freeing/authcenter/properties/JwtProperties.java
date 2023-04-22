package com.freeing.authcenter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jwt
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * token 加密密钥
     */
    private String secret;

    private int accessTokenExpire;

    private int refreshTokenExpire;
}
