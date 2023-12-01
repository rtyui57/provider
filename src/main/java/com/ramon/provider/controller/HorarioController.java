package com.ramon.provider.controller;

import com.ramon.provider.converters.HorarioConverter;
import com.ramon.provider.manager.horario.HorarioManager;
import com.ramon.provider.model.Horario;
import com.ramon.provider.rs.entity.RSHorario;
import com.ramon.provider.rs.entity.RSHorarioUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/horario")
public class HorarioController {

    @Autowired
    protected HorarioManager horarioManager;

    @Autowired
    protected HorarioConverter horarioConverter;

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable String id) {
        horarioManager.delete(id);
    }

    @GetMapping(path = "/{id}")
    public RSHorarioUsers find(@PathVariable String id) {
        return horarioConverter.convertFull(horarioManager.find(id));
    }

    @GetMapping
    public List<RSHorario> findAll() {
        List<RSHorario> horarios = new ArrayList<>();
        horarioManager.findAll().forEach(h -> horarios.add(horarioConverter.convert(h)));
        return horarios;
    }

    @PostMapping
    public void importHorario(@RequestBody RSHorario horario) {
        horarioManager.importHorario(horario);
    }

    @GetMapping("/states")
    public Horario.AttendantState[] getStates() {
        return Horario.AttendantState.values();
    }

    @PostMapping("/{id}/attendants")
    public void saveAttendants(@PathVariable String id, @RequestBody Map<String, Horario.AttendantState> attendants) {
        horarioManager.saveAttendants(id, attendants);
    }
}
