package com.ramon.provider.manager.horario;

import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.CommonManager;
import com.ramon.provider.model.Horario;
import com.ramon.provider.model.User;
import com.ramon.provider.rs.entity.RSHorario;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HorarioManager {

    @Autowired
    protected HorarioRepository horarioRepository;

    @Autowired
    protected CommonManager commonManager;

    public void importHorario(RSHorario newHorario) {
        if (ObjectUtils.isEmpty(newHorario.getAsignatura()) || ObjectUtils.isEmpty(newHorario.getTitle())) {
            throw new ResourceNotFoundException("No asignatura or aula");
        }
        Horario horario;
        if (!ObjectUtils.isEmpty(newHorario.getId()) && horarioRepository.existsById(newHorario.getId())) {
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
        commonManager.saveHorario(horario, newHorario.getAsignatura(), newHorario.getAula());
        setAttendants(horario);
        saveHorario(horario);
    }

    public void delete(String id) {
        Horario horario = horarioRepository.findById(id).get();
        commonManager.removeHorario(horario);
    }

    public void delete(Horario horario) {
        horarioRepository.delete(horario);
    }

    public void deleteAll() {
        horarioRepository.findAll().forEach(hor -> commonManager.removeHorario(hor));
    }

    public Horario find(String id) {
        return horarioRepository.findById(id).get();
    }

    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    public void setAttendants(Horario horario) {
        List<User> attendants = new ArrayList<>();
        attendants.addAll(horario.getAsignatura().getAlumnos());
        attendants.addAll(horario.getAsignatura().getProfesores());
        for (User user : attendants) {
            horario.getAttendants().put(user.getUsername(), Horario.AttendantState.TO_ATTEND);
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
