package com.hmall.common.utils;

import org.springframework.amqp.core.MessagePostProcessor;

/**
 *
 * @author FragrantXue
 * Create by 2025.09.25 03:08
 */

public class MQUtils {

    public static MessagePostProcessor setMessageDelay(int delayMillisecond) {
        return message -> {
            message.getMessageProperties().setDelay(delayMillisecond);
            return message;
        };
    }
}
