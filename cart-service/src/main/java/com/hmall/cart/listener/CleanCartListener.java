package com.hmall.cart.listener;

import com.hmall.cart.service.ICartService;
import com.hmall.common.domain.CartCleanMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.23 12:17
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class CleanCartListener {

    private final ICartService cartService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "cart.clear.queue", durable = "true"),
            exchange = @Exchange(value = "trade.topic", durable = "true", type = "topic"),
            key = {"order.create"}
    ))
    public void listenCleanCart(CartCleanMessage msg) {
        Long userId = msg.getUserId();
        Collection<Long> itemIds = msg.getItemIds();
        if (userId == null || itemIds == null) {
            log.warn("购物车服务-监听到清理购物车消息，消息内容为空");
            return;
        }

        log.info("购物车服务-监听到清理购物车消息，用户id：{}，商品id集合：{}", userId, itemIds);
        cartService.removeByItemIds(userId, itemIds);
    }
}
