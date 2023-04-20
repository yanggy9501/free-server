package com.freeing.authcenter.utils;

import com.freeing.common.web.configuration.I18nLocaleResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author yanggy
 */
@Component
public class I18Utils {
    private static final Logger logger = LoggerFactory.getLogger(I18Utils.class);

    @Value("${spring.messages.basename}")
    private String basename;

    private final I18nLocaleResolver resolver;

    private static I18nLocaleResolver customLocaleResolver;

    private static String path;


    public I18Utils(I18nLocaleResolver localeResolver) {
        this.resolver = localeResolver;
    }

    @PostConstruct
    public void init() {
        setBasename(basename);
        setCustomLocaleResolver(resolver);
    }

    /**
     * 获取 国际化后内容信息
     *
     * @param code 国际化key
     * @return 国际化后内容信息
     */
    public static String getMessage(String code) {
        Locale locale = customLocaleResolver.getLocal();
        return getMessage(code, null, code, locale);
    }

    /**
     * 获取指定语言中的国际化信息，如果没有则走英文
     *
     * @param code 国际化 key
     * @param lang 语言参数
     * @return 国际化后内容信息
     */
    public static String getMessage(String code, String lang) {
        Locale locale;
        if (StringUtils.isEmpty(lang)) {
            locale = Locale.US;
        } else {
            try {
                String[] split = lang.split("_");
                locale = new Locale(split[0], split[1]);
            } catch (Exception e) {
                locale = Locale.US;
            }
        }
        return getMessage(code, null, code, locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        messageSource.setBasename(path);
        String content;
        try {
            content = messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            logger.error("国际化参数获取失败", e);
            content = defaultMessage;
        }
        return content;
    }

    public static void setBasename(String basename) {
        I18Utils.path = basename;
    }

    public static void setCustomLocaleResolver(I18nLocaleResolver resolver) {
        I18Utils.customLocaleResolver = resolver;
    }
}
