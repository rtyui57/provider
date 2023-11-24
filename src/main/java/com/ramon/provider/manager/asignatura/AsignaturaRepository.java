package com.ramon.provider.manager.asignatura;

import com.ramon.provider.model.Asignatura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository  extends MongoRepository<Asignatura, String> {
}
