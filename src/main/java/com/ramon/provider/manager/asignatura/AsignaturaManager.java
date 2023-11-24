package com.ramon.provider.manager.asignatura;

import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.horario.HorarioManager;
import com.ramon.provider.manager.user.UserManager;
import com.ramon.provider.model.Asignatura;
import com.ramon.provider.model.Horario;
import com.ramon.provider.model.User;
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
    protected HorarioManager horarioManager;

    @Autowired
    protected UserManager userManager;

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
        repository.save(asignatura);
    }

    public List<Asignatura> list() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Asignatura find(String id) {
        Optional<Asignatura> asignatura = repository.findById(id);
        if (asignatura.isEmpty()) {
            throw new ResourceNotFoundException("Asginatura no existe");
        }
        return asignatura.get();
    }

    public void addHorario(String id, Horario horario) {
        Asignatura asignatura = find(id);
        horario.setAsignatura(asignatura);
        //horarioManager.importHorario(horario);
        asignatura.addHorario(horario);
        repository.save(asignatura);
    }

    public void addProfesor(String asignaturaId, String userId) {
        User user = userManager.find(userId);
        Asignatura asignatura = find(asignaturaId);
        asignatura.addProfesor(user);
        repository.save(asignatura);
    }

    public void addAlumno(String asignaturaId, String userId) {
        User user = userManager.find(userId);
        Asignatura asignatura = find(asignaturaId);
        asignatura.addAlumno(user);
        repository.save(asignatura);
    }

    public List<String> getNombres() {
        List<String> nombres = new ArrayList<>();
        repository.findAll().forEach(a -> nombres.add(a.getName()));
        return nombres;
    }
}
