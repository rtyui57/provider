package com.ramon.provider.controller;

import com.ramon.provider.manager.horario.HorarioManager;
import com.ramon.provider.model.Horario;
import com.ramon.provider.rs.entity.RSHorario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horario")
public class HorarioController {

    @Autowired
    protected HorarioManager horarioManager;

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable String id) {
        horarioManager.delete(id);
    }

    @GetMapping(path = "/{id}")
    public Horario find(@PathVariable String id) {
        return horarioManager.find(id);
    }

    @GetMapping
    public List<Horario> findAll() {
        return horarioManager.findAll();
    }

    @PostMapping
    public void importHorario(@RequestBody RSHorario horario) {
        horarioManager.importHorario(horario);
    }
}
