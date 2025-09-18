package com.hmall.api.config;

import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.16 15:18
 */

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Long user = UserContext.getUser();
                if (user == null) {
                    return;
                }
                template.header("User-Info", user.toString());
            }
        };
    }
}
