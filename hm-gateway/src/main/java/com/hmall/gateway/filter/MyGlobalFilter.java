// package com.hmall.gateway.filter;
//
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// /**
//  *
//  * @author FragrantXue
//  * Create by 2025.09.17 11:32
//  */
//
// @Component
// public class MyGlobalFilter implements GlobalFilter, Ordered {
//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         // 模拟登录校验逻辑
//         ServerHttpRequest request = exchange.getRequest();
//         HttpHeaders headers = request.getHeaders();
//         System.out.println("headers = " + headers);
//         return chain.filter(exchange);
//     }
//
//     @Override
//     public int getOrder() {
//         return Ordered.HIGHEST_PRECEDENCE;
//     }
// }
