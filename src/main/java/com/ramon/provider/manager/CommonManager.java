package com.ramon.provider.manager;

import com.ramon.provider.manager.asignatura.AsignaturaManager;
import com.ramon.provider.manager.building.AulaRepo;
import com.ramon.provider.manager.building.BuildingManager;
import com.ramon.provider.manager.horario.HorarioManager;
import com.ramon.provider.manager.user.UserManager;
import com.ramon.provider.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CommonManager {

    @Autowired
    protected HorarioManager horarioManager;

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected AsignaturaManager asignaturaManager;

    @Autowired
    protected BuildingManager buildingManager;

    @Autowired
    protected AulaRepo aulaRepo;

    public void saveHorario(Horario horario, String asignaturaId, String aulaId) {
        //Asignatura
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        horario.setAsignatura(asignatura);
        asignatura.addHorario(horario);
        asignaturaManager.saveAsignatura(asignatura);
        //Building
        Building building = buildingManager.find(aulaId.split("--")[0]);
        for (Aula aula : building.getAulas()) {
            if (Objects.equals(aula.getName(), aulaId.split("--")[1])) {
                aula.getHorarios().add(horario);
                horario.setAula(aula);
                aulaRepo.save(aula);
            }
        }
        buildingManager.saveBuilding(building);
        //Attendants
        List<User> attendants = new ArrayList<>();
        attendants.addAll(horario.getAsignatura().getAlumnos());
        attendants.addAll(horario.getAsignatura().getProfesores());
        for (User user : attendants) {
            horario.getAttendants().put(user.getId(), Horario.AttendantState.TO_ATTEND);
        }
        horarioManager.saveHorario(horario);
    }

}
