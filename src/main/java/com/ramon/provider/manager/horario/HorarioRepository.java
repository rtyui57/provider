package com.ramon.provider.manager.horario;

import com.ramon.provider.model.Horario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends MongoRepository<Horario, String> {
}
