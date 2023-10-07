package com.ramon.provider.manager;

import com.ramon.provider.manager.repository.DeviceRepository;
import com.ramon.provider.model.Device;
import com.ramon.provider.model.DeviceRegister;
import org.apache.commons.lang3.ObjectUtils;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class DeviceManager {

    @Autowired
    protected DeviceRepository deviceRepository;

    public void importDevice(Device newDevice) {
        if (ObjectUtils.isEmpty(newDevice.getName())) {
            throw new RuntimeException("User must have name");
        }
        Device device;
        Optional<Device> opt = deviceRepository.findById(newDevice.getName());
        if (opt.isEmpty()) {
            device = new Device();
            device.setName(newDevice.getName());
            device.setCreationDate(new Date());
        } else {
            device = opt.get();
        }
        device.setCategory(newDevice.getCategory());
        device.setCustomer(newDevice.getCustomer());
        device.setDescription(newDevice.getDescription());
        device.setOriginCountry(newDevice.getOriginCountry());
        device.setLastModificationDate(new Date());
        deviceRepository.save(newDevice);
    }

    public Device findDevice(String id) {
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isEmpty()) {
            throw new OpenApiResourceNotFoundException(MessageFormat.format("Device not found with id {0}", id));
        }
        return device.get();
    }

    public List<Device> findByCustomer(String customer) {
        return deviceRepository.findByCustomer(customer);
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public void addRegister(String id, DeviceRegister deviceRegister) {
        Device device = findDevice(id);
        device.addRegistro(deviceRegister);
        deviceRepository.save(device);
    }

    public void deleteDevice(String id) {
        deviceRepository.deleteById(id);
    }
}
