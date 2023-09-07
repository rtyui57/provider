package com.ramon.provider.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void receiveMessage(String message) {
        logger.warn("Received < {} >", message);
    }
}
