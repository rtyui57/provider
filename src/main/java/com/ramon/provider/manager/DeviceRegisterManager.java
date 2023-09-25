package com.ramon.provider.manager;

import com.ramon.provider.manager.repository.PostRepository;
import com.ramon.provider.model.DeviceRegister;
import com.ramon.provider.rabbitmq.JSONUtils;
import com.ramon.provider.rabbitmq.RabbitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceRegisterManager {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    public void create(DeviceRegister deviceRegister) {
        rabbitTemplate.convertAndSend(RabbitConfiguration.exchangeName, "com.ramon.deviceRegister.create", JSONUtils.toJSON(deviceRegister));
        logger.error("Recivido regiustro");
    }
}
