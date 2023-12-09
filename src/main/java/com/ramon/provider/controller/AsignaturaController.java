package com.ramon.provider.controller;

import com.ramon.provider.converters.AsignaturaConverter;
import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.CommonManager;
import com.ramon.provider.manager.asignatura.AsignaturaManager;
import com.ramon.provider.model.Asignatura;
import com.ramon.provider.rs.entity.RSAsignatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {

    @Autowired
    protected AsignaturaManager asignaturaManager;

    @Autowired
    AsignaturaConverter converter;

    @Autowired
    CommonManager commonManager;

    @GetMapping
    public List<RSAsignatura> list() {
        return converter.convert(asignaturaManager.list());
    }

    @GetMapping("/{id}")
    public RSAsignatura find(@PathVariable String id) {
        return converter.convert(asignaturaManager.find(id));
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
        commonManager.addProfesor(asignaturaId, userId);
    }

    @PostMapping("/{asignaturaId}/alumno/{userId}")
    public void addAlumno(@PathVariable String asignaturaId, @PathVariable String userId) {
        commonManager.addAlumno(asignaturaId, userId);
    }

    @DeleteMapping("/{asignaturaId}/alumno/{userId}")
    public void removeAlumno(@PathVariable String asignaturaId, @PathVariable String userId) {
        commonManager.removeAlumno(asignaturaId, userId);
    }

    @DeleteMapping("/{asignaturaId}/profesor/{userId}")
    public void removeProfesor(@PathVariable String asignaturaId, @PathVariable String userId) {
        commonManager.removeProfesor(asignaturaId, userId);
    }

    @GetMapping("/nombres")
    public List<String> getNombres() {
        return asignaturaManager.getNombres();
    }

    @DeleteMapping
    public void removeAll() {
        asignaturaManager.deleteAll();
    }

    @GetMapping(path = "asdljkfhsuidfgh")
    public void test() {
        throw new ResourceNotFoundException("Not found");
    }
}
