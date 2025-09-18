package com.hmall.gateway.router;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.19 00:01
 */

@Component
@Slf4j
@RequiredArgsConstructor
// -1. 初始化Bean
public class DynamicRouteLoader {

    private final NacosConfigManager nacosConfigManager;

    private final RouteDefinitionWriter routeDefinitionWriter;

    private final String dataId = "gateway-routes.json";

    private final String group = "DEFAULT_GROUP";

    private final Set<String> routeIds = new HashSet<>();

    // 0. 在本类对应的Bean初始化后，执行初始化路由配置监听器
    @PostConstruct
    public void initRouteConfigListener() throws NacosException {
        // 1. 项目启动时，先拉取一次配置，并添加一个配置监听器
        String configInfo = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    // 当配置发生变化时，触发该方法
                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        // 2. 当配置发生变化时，更新路由表
                        log.debug("路由配置发生变化，新的路由配置为：\n{}", configInfo);
                        updateConfigInfo(configInfo);
                    }
                });
        // 3. 项目启动时，加载一次路由配置
        log.debug("初始化加载路由配置：\n{}", configInfo);
        updateConfigInfo(configInfo);
    }

    /* 更新路由表配置的具体实现 */
    public void updateConfigInfo(String configInfo) {
        log.debug("更新路由配置：\n{}", configInfo);

        // 1. 解析配置文件并转换为RouteDefinition列表
        List<RouteDefinition> routeDefinitions =
                JSONUtil.toList(configInfo, RouteDefinition.class);

        // 2. 先删除旧的路由配置
        routeIds.forEach(routeId -> {
            log.debug("删除旧的路由配置，路由ID：{}", routeId);
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        });
        routeIds.clear();

        // 3. 再更新路由表
        routeDefinitions.forEach(routeDefinition -> {
            // 3.1 保存新的路由配置
            routeDefinitionWriter
                    // Mono是响应式编程中的一个概念，表示一个异步操作的容器
                    // 此处将RouteDefinition对象放入Mono容器中
                    .save(Mono.just(routeDefinition))
                    // 然后订阅该Mono以触发保存操作
                    .subscribe();

            // 3.2 记录路由id以便下次删除
            log.debug("更新路由配置成功，路由ID：{}", routeDefinition.getId());
            routeIds.add(routeDefinition.getId());
        });
    }
}
