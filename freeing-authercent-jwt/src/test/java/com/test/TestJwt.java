package com.test;

import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author yanggy
 */
public class TestJwt {
    public static void main(String[] args) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 加密密钥（可以采用属性注入方式生产中建议加密）
        converter.setSigningKey("123abcd");

        JwtHelper.encode("", ne);
    }
}
