package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.16 15:18
 */

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.FULL;
    }
}
