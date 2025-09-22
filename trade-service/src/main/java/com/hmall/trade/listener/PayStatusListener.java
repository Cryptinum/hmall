package com.hmall.trade.listener;

import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.23 03:10
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class PayStatusListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "trade.pay.success.queue", durable = "true"),
            exchange = @Exchange(name = "pay.direct", durable = "true", type = "direct"),
            key = {"pay.success"}
    ))
    public void listenPaySuccess(Long orderId) {
        log.info("接收到支付成功的消息，订单ID：{}", orderId);
        orderService.markOrderPaySuccess(orderId);
    }
}
