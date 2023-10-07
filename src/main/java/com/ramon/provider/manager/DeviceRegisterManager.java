package com.ramon.provider.manager;

import com.ramon.provider.model.Device;
import com.ramon.provider.model.DeviceRegister;
import com.ramon.provider.rabbitmq.config.*;
import com.ramon.provider.rabbitmq.manager.RabbitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeviceRegisterManager {

    @Autowired
    protected RabbitManager rabbitManager;

    @Autowired
    protected DeviceManager deviceManager;

    protected RabbitConnection rabbitConnection;
    protected RabbitConfig rabbitConfig;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    public void create(String deviceId, DeviceRegister deviceRegister) {
        deviceManager.addRegister(deviceId, deviceRegister);
        rabbitManager.sendMessage(getRabbitConnection(), getRabbitConfig(), buildMessage(deviceRegister));
        logger.debug("Recibido nuevo registro para el dispositivo {} a las {}", deviceId, new Date());
    }

    protected RabbitConnection getRabbitConnection() {
        if (rabbitConnection == null) {
            try {
                rabbitManager.createResources(getRabbitConfig());
                rabbitConnection = rabbitManager.newSendConnection(getRabbitConfig(), "");
            } catch (Exception e) {
                logger.error("Error stablishing connection", e);
            }
        }
        return rabbitConnection;
    }

    protected RabbitConfig getRabbitConfig() {
        if (rabbitConfig == null) {
            rabbitConfig = new RabbitConfig();
            rabbitConfig.setQueue("provider");
            rabbitConfig.setExchange("com.ramon.provider");
            rabbitConfig.setRoutingKey("com.ramon.deviceRegister.*");
        }
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
        rabbitMessage.setRoutingKey("com.ramon.deviceRegister.create");
        return rabbitMessage;
    }
}
