package com.ramon.provider.manager.repository;

import com.ramon.provider.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceRepository extends MongoRepository<Device,String> {

    List<Device> findByCustomer(String customer);
}
