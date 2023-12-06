package com.ramon.provider.manager.asignatura;

import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.CommonManager;
import com.ramon.provider.model.Asignatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AsignaturaManager {

    @Autowired
    protected AsignaturaRepository repository;

    @Autowired
    protected CommonManager commonManager;

    public void saveAsignatura(Asignatura asignatura) {
        repository.save(asignatura);
    }

    public void importAsignatura(Asignatura newAsignatura) {
        Asignatura asignatura;
        Optional<Asignatura> opt = repository.findById(newAsignatura.getName());
        if (opt.isPresent()) {
            asignatura = opt.get();
        } else {
            asignatura = new Asignatura();
            asignatura.setId(newAsignatura.getName());
            asignatura.setName(newAsignatura.getName());
        }
        asignatura.getAlumnos().addAll(newAsignatura.getAlumnos());
        asignatura.getProfesores().addAll(newAsignatura.getProfesores());
        asignatura.getHorarios().addAll(newAsignatura.getHorarios());
        asignatura.setDisplayName(newAsignatura.getDisplayName());
        asignatura.setCurso(newAsignatura.getCurso());
        asignatura.setGrado(newAsignatura.getGrado());
        asignatura.setCreditos(newAsignatura.getCreditos());
        repository.save(asignatura);
    }

    public List<Asignatura> list() {
        return repository.findAll();
    }

    public void delete(String id) {
        Asignatura asignatura = repository.findById(id).get();
        commonManager.removeAsignatura(asignatura);
    }

    public void delete(Asignatura asignatura) {
        repository.delete(asignatura);
    }

    public void deleteAll() {
        repository.findAll().forEach(as -> commonManager.removeAsignatura(as));
    }

    public Asignatura find(String id) {
        Optional<Asignatura> asignatura = repository.findById(id);
        if (asignatura.isEmpty()) {
            throw new ResourceNotFoundException("Asginatura no existe");
        }
        return asignatura.get();
    }

    public List<String> getNombres() {
        List<String> nombres = new ArrayList<>();
        repository.findAll().forEach(a -> nombres.add(a.getName()));
        return nombres;
    }
}
