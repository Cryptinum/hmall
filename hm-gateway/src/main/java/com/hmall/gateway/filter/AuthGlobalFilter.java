package com.hmall.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.17 13:22
 */

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求和响应
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 2. 判断放行路径
        String path = request.getPath().toString();
        if (isExcludePath(path)) {
            return chain.filter(exchange);
        }

        // 3. 从请求头中获取token
        String token = null;
        List<String> auths = request.getHeaders().get("Authorization");
        if (auths != null && !auths.isEmpty()) {
            token = auths.get(0);
        }

        try {
            // 4. 校验token
            Long userId = jwtTool.parseToken(token);

            // 5. 传递用户信息
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(builder -> builder.header("User-Info", userId.toString()))
                    .build();

            // 6. 放行
            return chain.filter(mutatedExchange);
        } catch (Exception e) {
            // 4.1 如果有异常，拦截设置响应状态为401，结束响应
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    private boolean isExcludePath(String path) {
        // 配置中的路径是一种特殊的路径匹配方式，称为ant风格
        // 使用Spring提供的AntPathMatcher来实现路径匹配
        // 如果匹配上了，就放行
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, path)) {
                return true;
            }
        }
        // 一个都没匹配上
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
