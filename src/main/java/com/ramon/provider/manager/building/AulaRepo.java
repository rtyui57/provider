package com.ramon.provider.manager.building;

import com.ramon.provider.model.Aula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface AulaRepo extends MongoRepository<Aula, String> {
}
