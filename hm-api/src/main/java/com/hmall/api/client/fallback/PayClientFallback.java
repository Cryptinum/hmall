package com.hmall.api.client.fallback;

import com.hmall.api.client.PayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.25 03:28
 */

@Slf4j
public class PayClientFallback implements FallbackFactory<PayClient> {
    @Override
    public PayClient create(Throwable cause) {
        return id -> null;
    }
}
