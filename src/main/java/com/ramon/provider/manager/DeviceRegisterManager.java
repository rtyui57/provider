package com.ramon.provider.manager;

import com.ramon.provider.model.DeviceRegister;
import com.ramon.provider.rabbitmq.config.*;
import com.ramon.provider.rabbitmq.manager.RabbitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceRegisterManager {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected RabbitManager rabbitManager;

    protected RabbitConnection rabbitConnection;


    public void create(DeviceRegister deviceRegister) {
        rabbitManager.sendMessage(getRabbitConnection(), getRabbitConfig(), buildMessage(deviceRegister));
        logger.error("Recivido regiustro");
    }

    protected RabbitConnection getRabbitConnection() {
        if (rabbitConnection == null) {
            rabbitConnection = rabbitManager.newSendConnection(getRabbitConfig(), "");
        }
        return rabbitConnection;
    }

    protected RabbitConfig getRabbitConfig() {
        RabbitConfig rabbitConfig = new RabbitConfig();
        rabbitConfig.setQueue("provider");
        rabbitConfig.setExchange("com.ramon.provider");
        rabbitConfig.setRoutingKey("com.ramon.deviceRegister.create");
        return rabbitConfig;
    }

    protected RabbitMessage buildMessage(DeviceRegister deviceRegister) {
        RabbitMessage rabbitMessage = new RabbitMessage();
        RabbitEvent rabbitEvent = new RabbitEvent();
        rabbitEvent.setAction(RabbitAction.CREATE);
        rabbitEvent.setService("provider");
        rabbitEvent.setType("deviceRegister");
        rabbitEvent.setObject(deviceRegister);
        rabbitMessage.setBody(rabbitEvent);
        rabbitMessage.setRoutingKey("com.ramon.deviceRegister.*");
        return rabbitMessage;
    }
}
