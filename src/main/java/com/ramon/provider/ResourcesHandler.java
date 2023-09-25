package com.ramon.provider;

import com.ramon.provider.rabbitmq.config.RabbitConfig;
import com.ramon.provider.rabbitmq.manager.RabbitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ResourcesHandler implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    protected RabbitManager rabbitManager;
    protected static final Logger logger = LoggerFactory.getLogger(ResourcesHandler.class);
    protected boolean started = false;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //startRabbit();
    }

    public void startRabbit() {
        if (!started) {
            try {
                RabbitConfig rabbitConfig = new RabbitConfig();
                rabbitConfig.setQueue("provider");
                rabbitConfig.setExchange("com.ramon.provider");
                rabbitConfig.setRoutingKey("com.ramon.deviceRegister.*");
                rabbitManager.createResources(rabbitConfig);
                started = true;
            } catch (Exception ex) {
                logger.error("Error starting rabbitmq for Addons notifications. Important: Addons cannot be synchronized.", ex);
            }
        }
    }
}
