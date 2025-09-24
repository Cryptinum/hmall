package com.hmall.common.constants;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.25 02:56
 */

public interface MQConstants {
    String TRADE_EXCHANGE_NAME = "trade.topic";
    String ORDER_CREATE_KEY = "order.create";

    String PAY_EXCHANGE_NAME = "pay.direct";
    String PAY_SUCCESS_KEY = "pay.success";

    String DELAY_EXCHANGE_NAME = "trade.delay.direct";
    String DELAY_QUEUE_NAME = "trade.delay.order.queue";
    String DELAY_ROUTING_KEY = "delay.order.query";
}
