package com.ramon.provider.converters;

import com.ramon.provider.model.Asignatura;
import com.ramon.provider.rs.entity.RSAsignatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsignaturaConverter {

    @Autowired
    HorarioConverter horarioConverter;

    @Autowired
    UserConverter userConverter;

    public RSAsignatura convert(Asignatura asignatura) {
        RSAsignatura res = new RSAsignatura();
        res.setId(asignatura.getId());
        res.setName(asignatura.getName());
        res.setDisplayName(asignatura.getDisplayName());
        res.setHorarios(horarioConverter.convert(asignatura.getHorarios()));
        res.setCreditos(asignatura.getCreditos());
        res.setCurso(asignatura.getCurso());
        res.setGrado(asignatura.getGrado());
        res.setAlumnos(userConverter.convert(asignatura.getAlumnos()));
        res.setProfesores(userConverter.convert(asignatura.getProfesores()));
        return res;
    }

    public List<RSAsignatura> convert(List<Asignatura> asignaturas) {
        List<RSAsignatura> res = new ArrayList<>();
        asignaturas.forEach(h -> res.add(convert(h)));
        return res;
    }
}
