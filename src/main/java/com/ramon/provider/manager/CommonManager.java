package com.ramon.provider.manager;

import com.ramon.provider.manager.asignatura.AsignaturaManager;
import com.ramon.provider.manager.building.AulaManager;
import com.ramon.provider.manager.building.BuildingManager;
import com.ramon.provider.manager.horario.HorarioManager;
import com.ramon.provider.manager.user.UserManager;
import com.ramon.provider.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    protected AulaManager aulaManager;

    public void saveHorario(Horario horario, String asignaturaId, String aulaId) {
        //Asignatura
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        horario.setAsignatura(asignatura);
        asignatura.addHorario(horario);
        asignaturaManager.saveAsignatura(asignatura);
        //Building
        Building building = buildingManager.find(aulaId.split("--")[0]);
        for (Aula aula : building.getAulas()) {
            if (Objects.equals(aula.getId(), aulaId)) {
                aula.getHorarios().add(horario);
                horario.setAula(aula);
                aulaManager.saveAula(aula);
            }
        }
        buildingManager.saveBuilding(building);
        horarioManager.saveHorario(horario);
    }

    public void removeHorario(Horario horario) {
        //Asignatura
        Asignatura asignatura = horario.getAsignatura();
        asignatura.getHorarios().remove(horario);
        asignaturaManager.saveAsignatura(asignatura);
        //Building
        Aula aula = horario.getAula();
        Building building = aula.getBuilding();
        aula.getHorarios().remove(horario);
        aulaManager.saveAula(aula);
        buildingManager.saveBuilding(building);
        horarioManager.delete(horario);
    }

    public void removeAsignatura(Asignatura asignatura) {
        List<User> profesores = asignatura.getProfesores();
        List<User> alumnos = asignatura.getAlumnos();
        for (User user : profesores) {
            user.getAsignaturas().remove(asignatura);
            userManager.save(user);
        }
        for (User user : alumnos) {
            user.getAsignaturas().remove(asignatura);
            userManager.save(user);
        }
        asignatura.getHorarios().forEach(this::removeHorario);
        asignaturaManager.delete(asignatura);
    }

    public void removeUser(User user) {
        List<Asignatura> asignaturas = user.getAsignaturas();
        for (Asignatura asignatura : asignaturas) {
            if (asignatura.getAlumnos().contains(user)) {
                asignatura.getAlumnos().remove(user);
                asignaturaManager.saveAsignatura(asignatura);
            }
            if (asignatura.getProfesores().contains(user)) {
                asignatura.getProfesores().remove(user);
                asignaturaManager.saveAsignatura(asignatura);
            }
        }
        userManager.deleteUser(user);
    }

    public void removeAula(Aula aula) {
        List<Horario> horarios = aula.getHorarios();
        horarios.forEach(this::removeHorario);
        Building building = aula.getBuilding();
        building.getAulas().remove(aula);
        buildingManager.saveBuilding(building);
        aulaManager.saveAula(aula);
    }

    public void removeBuilding(Building building) {
        building.getAulas().forEach(this::removeAula);
        buildingManager.delete(building);
    }

    public void removeProfesor(String asignaturaId, String userId) {
        User user = userManager.find(userId);
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        asignatura.removeProfesor(userId);
        asignaturaManager.saveAsignatura(asignatura);
        user.removeAsignatura(asignaturaId);
        userManager.save(user);
    }

    public void removeAlumno(String asignaturaId, String userId) {
        User user = userManager.find(userId);
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        asignatura.removeAlumno(userId);
        asignaturaManager.saveAsignatura(asignatura);
        user.removeAsignatura(asignaturaId);
        userManager.save(user);
    }

    public void addProfesor(String asignaturaId, String userId) {
        User profesor = userManager.find(userId);
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        if (!asignatura.getProfesores().stream().map(User::getUsername).toList().contains(userId) && !asignatura.getAlumnos().stream().map(User::getUsername).toList().contains(userId) && Objects.equals(profesor.getPuesto(), User.PUESTO.PROFESOR)) {
            profesor.getAsignaturas().add(asignatura);
            userManager.save(profesor);
            asignatura.addProfesor(profesor);
            asignaturaManager.saveAsignatura(asignatura);
        } else {
            throw new RuntimeException("El usuario debe ser Profesor o no estar ya incluido en la lista");
        }
    }

    public void addAlumno(String asignaturaId, String userId) {
        User alumno = userManager.find(userId);
        Asignatura asignatura = asignaturaManager.find(asignaturaId);
        if (!asignatura.getProfesores().stream().map(User::getUsername).toList().contains(userId) && !asignatura.getAlumnos().stream().map(User::getUsername).toList().contains(userId) && Objects.equals(alumno.getPuesto(), User.PUESTO.ESTUDIANTE)) {
            alumno.getAsignaturas().add(asignatura);
            userManager.save(alumno);
            asignatura.addAlumno(alumno);
            asignaturaManager.saveAsignatura(asignatura);
        } else {
            throw new RuntimeException("El usuario debe ser Alumon o no estar ya incluido en la lista");
        }
    }
}