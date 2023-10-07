package com.ramon.provider.model.listeners;

import com.ramon.provider.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.stereotype.Component;

@Component
public class DeviceListener extends AbstractMongoEventListener<Device> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAfterSave(AfterSaveEvent<Device> event) {
        logger.debug("sadfasf");
    }

    @Override
    public void onAfterLoad(AfterLoadEvent<Device> event) {
        logger.debug("sadfasf");
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Device> event) {
        logger.debug("sadfasf");
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Device> event) {
        logger.debug("ad");

    }

}