package com.freeing.authcenter.utils;

import com.alibaba.fastjson.JSON;
import com.freeing.common.web.utils.ServletUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 权限获取工具类
 *
 * @author yanggy
 */
@Slf4j
public class SecurityUtils {
    private static final String AUTHENTICATION = "Authorization";

    private static final String PREFIX = "Bearer";

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    private static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(PREFIX)) {
            token = token.replaceFirst(PREFIX, "").trim();
        }
        return token;
    }

    public static SecurityUtils.ExUser getUser() {
        // 拿jwt中的用户身份
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUtils.ExUser xUser = null;
        if (principal instanceof String){
            String jsonString = (String) principal;
            try {
                xUser = JSON.parseObject(jsonString, SecurityUtils.ExUser.class);
            } catch (Exception ignored) {
                log.debug("jwt中的用户身份无法解析成 ExUser 对象:{}", jsonString);
            }
        }
        return xUser;
    }

    @Data
    public static class ExUser implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;

        private String username;

        private String nickname;

        private String password;

        private String salt;

        /**
         * 微信 ID
         */
        private String wxUnionId;

        /**
         * 头像
         */
        private String pic;

        private String type;

        private Date birthday;

        private String gender;

        private String email;

        private String phone;

        private String qq;

        /**
         * 用户状态
         */
        private String status;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
    }
}
