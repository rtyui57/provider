package com.ramon.provider.manager.horario;

import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.asignatura.AsignaturaManager;
import com.ramon.provider.manager.building.BuildingManager;
import com.ramon.provider.model.Asignatura;
import com.ramon.provider.model.Aula;
import com.ramon.provider.model.Building;
import com.ramon.provider.model.Horario;
import com.ramon.provider.rs.entity.RSHorario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

@Component
public class HorarioManager {

    @Autowired
    protected HorarioRepository horarioRepository;

    @Autowired
    protected AsignaturaManager asignaturaManager;

    @Autowired
    protected BuildingManager buildingManager;

    public void importHorario(RSHorario newHorario) {
        if (ObjectUtils.isEmpty(newHorario.getAsignatura()) || ObjectUtils.isEmpty(newHorario.getTitle())) {
            throw new ResourceNotFoundException("No asignatura or aula");
        }
        String id = genId(newHorario);
        Horario horario;
        if (horarioRepository.findById(id).isPresent()) {
            horario = horarioRepository.findById(id).get();
        } else {
            horario = new Horario();
            horario.setId(id);
        }
        horario.setTitle(newHorario.getTitle());
        horario.setColor(newHorario.getColor());
        horario.setDescripcion(newHorario.getDescripcion());
        horario.setStart(newHorario.getStart());
        horario.setEnd(newHorario.getEnd());
        addAsignatura(horario, newHorario.getAsignatura());
        addAula(horario, newHorario.getAula());
        horarioRepository.save(horario);
    }

    public void delete(String id) {
        horarioRepository.deleteById(id);
    }

    public Horario find(String id) {
        return horarioRepository.findById(id).get();
    }

    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    public static String genId(RSHorario horario) {
        return horario.getTitle() + "--" + horario.getAsignatura();
    }

    public void addAsignatura(Horario horario, String asignaturaId) {
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        horario.setAsignatura(asignatura);
        asignatura.addHorario(horario);
        asignaturaManager.saveAsignatura(asignatura);
    }

    public void addAula(Horario horario, String aulaId) {
        Building building = buildingManager.find(aulaId.split("--")[0]);
        for (Aula aula : building.getAulas()) {
            if (Objects.equals(aula.getName(), aulaId.split("--")[1])) {
                aula.getHorarios().add(horario);
                horario.setAula(aula);
            }
        }
        buildingManager.saveBuilding(building);
    }
}
