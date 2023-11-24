package com.ramon.provider.controller;

import com.ramon.provider.manager.asignatura.AsignaturaManager;
import com.ramon.provider.model.Asignatura;
import com.ramon.provider.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {

    @Autowired
    protected AsignaturaManager asignaturaManager;

    @GetMapping
    public List<Asignatura> list() {
        return asignaturaManager.list();
    }

    @GetMapping("/{id}")
    public Asignatura find(@PathVariable String id) {
        return asignaturaManager.find(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        asignaturaManager.delete(id);
    }

    @PostMapping
    public void importAsignatura(@RequestBody Asignatura asignatura) {
        asignaturaManager.importAsignatura(asignatura);
    }

    @PostMapping("/{asignaturaId}/profesor/{userId}")
    public void addProfesor(@PathVariable String asignaturaId, @PathVariable String userId) {
        asignaturaManager.addProfesor(asignaturaId, userId);
    }

    @PostMapping("/{asignaturaId}/alumno/{userId}")
    public void addAlumno(@PathVariable String asignaturaId, @PathVariable String userId) {
        asignaturaManager.addAlumno(asignaturaId, userId);
    }

    @PostMapping("/{asignaturaId}/horario")
    public void addAlumno(@PathVariable String asignaturaId, @RequestBody Horario horario) {
        asignaturaManager.addHorario(asignaturaId, horario);
    }

    @GetMapping("/nombres")
    public List<String> getNombres() {
        return asignaturaManager.getNombres();
    }
}
