package com.hmall.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * 封装购物车清理事件msg的pojo
 * 用于mq传输
 *
 * @author FragrantXue
 * Create by 2025.09.23 13:35
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCleanMessage implements Serializable {
    private Long userId;
    private Collection<Long> itemIds;
}
