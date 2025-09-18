package com.hmall.common.config;

import com.hmall.common.interceptor.UserInfoInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.17 14:44
 */

@Configuration
// 仅在类路径中存在 DispatcherServlet 时应用此配置
@ConditionalOnClass(DispatcherServlet.class)
// 仅在 Web 应用程序类型为 SERVLET 时应用此配置
// @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor())
                .addPathPatterns();
    }
}
