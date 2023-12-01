package com.ramon.provider.manager.horario;

import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.asignatura.AsignaturaManager;
import com.ramon.provider.manager.building.AulaRepo;
import com.ramon.provider.manager.building.BuildingManager;
import com.ramon.provider.model.*;
import com.ramon.provider.rs.entity.RSHorario;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Component
public class HorarioManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected HorarioRepository horarioRepository;

    @Autowired
    protected AsignaturaManager asignaturaManager;

    @Autowired
    protected BuildingManager buildingManager;

    @Autowired
    protected AulaRepo aulaRepo;

    public void importHorario(RSHorario newHorario) {
        if (ObjectUtils.isEmpty(newHorario.getAsignatura()) || ObjectUtils.isEmpty(newHorario.getTitle())) {
            throw new ResourceNotFoundException("No asignatura or aula");
        }
        Horario horario;
        if (!ObjectUtils.isEmpty(newHorario.getId())) {
            horario = horarioRepository.findById(newHorario.getId()).get();
        } else {
            horario = new Horario();
            horario.setId(new ObjectId().toHexString());
        }
        horario.setTitle(newHorario.getTitle());
        horario.setColor(newHorario.getColor());
        horario.setDescripcion(newHorario.getDescripcion());
        horario.setStart(newHorario.getStart());
        horario.setEnd(newHorario.getEnd());
        addAsignatura(horario, newHorario.getAsignatura());
        addAula(horario, newHorario.getAula());
        setAttendants(horario);
        horarioRepository.save(horario);
        logger.warn("Se ha guardado el nuevo horario");
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
                aulaRepo.save(aula);
            }
        }
        buildingManager.saveBuilding(building);
    }

    public void setAttendants(Horario horario) {
        List<User> attendants = new ArrayList<>();
        attendants.addAll(horario.getAsignatura().getAlumnos());
        attendants.addAll(horario.getAsignatura().getProfesores());
        for (User user : attendants) {
            horario.getAttendants().put(user.getId(), Horario.AttendantState.TO_ATTEND);
        }
    }

    public void saveAttendants(String id, Map<String, Horario.AttendantState> attendants) {
        Horario horario = find(id);
        Map<String, Horario.AttendantState> stateMap = new HashMap<>(horario.getAttendants());
        for (String user : attendants.keySet()) {
            stateMap.put(user, attendants.get(user));
        }
        horario.setAttendants(stateMap);
        horarioRepository.save(horario);
    }

    public void saveHorario(Horario horario) {
        horarioRepository.save(horario);
    }
}
