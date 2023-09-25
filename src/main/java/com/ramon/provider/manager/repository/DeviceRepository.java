package com.ramon.provider.manager.repository;

import com.ramon.provider.model.Device;
import com.ramon.provider.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device,String> {
}
