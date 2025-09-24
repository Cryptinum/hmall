package com.hmall.trade.listener;

import com.hmall.common.constants.MQConstants;
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
 * Create by 2025.09.25 03:15
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDelayMessageListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstants.DELAY_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = MQConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_ROUTING_KEY
    ))
    public void listenOrderDelayMessage(Long orderId) {
        orderService.listenOrderDelayMessage(orderId);
    }
}
