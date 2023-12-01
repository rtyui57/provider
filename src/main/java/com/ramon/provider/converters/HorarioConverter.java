package com.ramon.provider.converters;

import com.ramon.provider.model.Horario;
import com.ramon.provider.rs.entity.RSHorario;
import com.ramon.provider.rs.entity.RSHorarioUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HorarioConverter {

    @Autowired
    protected UserConverter userConverter;

    public RSHorario convert(Horario horario) {
        RSHorario result = new RSHorarioUsers();
        result.setId(horario.getId());
        result.setTitle(horario.getTitle());
        result.setDescripcion(horario.getDescripcion());
        result.setEnd(horario.getEnd());
        result.setStart(horario.getStart());
        result.setColor(horario.getColor());
        result.setAsignatura(horario.getAsignatura().getId());
        result.setAttendants(horario.getAttendants());
        result.setAula(horario.getAula().getId());
        return result;
    }

    public List<RSHorario> convert(List<Horario> horarioList) {
        List<RSHorario> res = new ArrayList<>();
        horarioList.forEach(h -> res.add(convert(h)));
        return res;
    }

    public RSHorarioUsers convertFull(Horario horario) {
        RSHorarioUsers horarioUsers = (RSHorarioUsers) convert(horario);
        horarioUsers.setAlumnos(userConverter.convert(horario.getAsignatura().getAlumnos()));
        horarioUsers.setProfesores(userConverter.convert(horario.getAsignatura().getProfesores()));
        return horarioUsers;
    }
}
