package com.freeing.authcenter.constants;

import java.util.Objects;

public enum AuthType {
    PASSWORD("password"),

    NOT_FOUND("not found");

    private String authType;

    AuthType(String authType) {
        this.authType = authType;
    }

    public static AuthType getType(String type) {
        AuthType[] types = AuthType.values();
        for (AuthType authType : types) {
            if (Objects.equals(type, authType.authType)) {
                return authType;
            }
        }
        return NOT_FOUND;
    }
}
