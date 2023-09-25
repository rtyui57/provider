package com.ramon.provider.manager;

import com.ramon.provider.manager.repository.DeviceRepository;
import com.ramon.provider.model.Device;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeviceManager {

    @Autowired
    protected DeviceRepository deviceRepository;

    public void save(Device device) {
        device.setId(device.getName());
        if (ObjectUtils.isEmpty(device.getCreationDate())) {
            device.setCreationDate(new Date());
        }
        device.setLastModificationDate(new Date());
        deviceRepository.save(device);
    }
}
