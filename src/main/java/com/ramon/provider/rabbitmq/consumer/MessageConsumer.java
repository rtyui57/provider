package com.ramon.provider.rabbitmq.consumer;

import com.ramon.provider.rabbitmq.config.RabbitMessage;

public interface MessageConsumer {

    String getOwner();

    void consumeMessage(RabbitMessage msg);
}
