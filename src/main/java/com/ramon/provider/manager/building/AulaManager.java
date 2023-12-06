package com.ramon.provider.manager.building;

import com.ramon.provider.model.Aula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AulaManager {

    @Autowired
    protected AulaRepo aulaRepo;

    public void saveAula(Aula aula) {
        aulaRepo.save(aula);
    }

    public void removeAula(Aula aula) {
        aulaRepo.delete(aula);
    }
}
