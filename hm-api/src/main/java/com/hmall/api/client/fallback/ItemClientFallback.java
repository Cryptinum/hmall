package com.hmall.api.client.fallback;

import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.20 17:08
 */

@Slf4j
public class ItemClientFallback implements FallbackFactory<ItemClient> {
    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClient() {
            @Override
            public List<ItemDTO> queryItemsByIds(Collection<Long> ids) {
                log.error("调用item-service的queryItemsByIds方法失败", cause);
                return CollUtils.emptyList();
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                log.error("调用item-service的deductStock方法失败", cause);
                throw new RuntimeException(cause);
            }

            @Override
            public void restoreStock(List<OrderDetailDTO> items) {
                log.error("调用item-service的restoreStock方法失败", cause);
                throw new RuntimeException(cause);
            }
        };
    }
}
