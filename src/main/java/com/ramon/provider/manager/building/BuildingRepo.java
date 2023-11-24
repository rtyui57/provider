package com.ramon.provider.manager.building;

import com.ramon.provider.model.Building;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface BuildingRepo extends MongoRepository<Building, String> {
}
