package com.ramon.provider.controller;

import com.ramon.provider.manager.DeviceManager;
import com.ramon.provider.manager.DeviceRegisterManager;
import com.ramon.provider.manager.repository.RedisRepo;
import com.ramon.provider.model.Device;
import com.ramon.provider.model.DeviceRegister;
import com.ramon.provider.rs.converter.DeviceRegisterConverter;
import com.ramon.provider.rs.entity.RSDeviceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    protected DeviceManager deviceManager;

    @Autowired
    protected DeviceRegisterManager deviceRegisterManager;

    @Autowired
    protected DeviceRegisterConverter deviceRegisterConverter;

    @Autowired
    protected RedisRepo redisRepo;

    @PostMapping(path = "/create")
    public void createDevice(@RequestBody Device device) {
        deviceManager.importDevice(device);
    }

    @GetMapping(path = "/list")
    public List<Device> listDevices(@RequestHeader String customer) {
        return deviceManager.findByCustomer(customer);
    }

    @GetMapping(path = "/{id}")
    public Device findDevice(@PathVariable String id) {
        return deviceManager.findDevice(id);
    }

    @PostMapping(path = "/{id}/register")
    public void createRegister(@PathVariable String id, @RequestBody RSDeviceRegister deviceRegister) {
        DeviceRegister register = deviceRegisterConverter.convert(deviceRegister);
        deviceRegisterManager.create(id, register);
    }

    @GetMapping(path = "/{id}/register/list")
    public List<DeviceRegister> getRegisters(@PathVariable String id, @RequestParam(required = false) int minutes) {
        return new ArrayList<>();
    }

    @DeleteMapping(path = "/{id}/remove")
    public void deleteDevice(@PathVariable String deviceId) {
        deviceManager.deleteDevice(deviceId);
    }
}
