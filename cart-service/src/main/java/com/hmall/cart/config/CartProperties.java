package com.hmall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.18 15:44
 */

@Data
@Component
@ConfigurationProperties(prefix = "hm.cart")
public class CartProperties {

    private Integer maxItems;
}
