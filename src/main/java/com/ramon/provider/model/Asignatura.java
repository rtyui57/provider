package com.ramon.provider.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document("asignatura")
public class Asignatura {

    @Id
    protected String id;
    protected String name;
    protected String displayName;
    protected String grado;
    protected String curso;
    protected Integer creditos;
    @DBRef
    protected List<User> profesores = new ArrayList<>();
    @DBRef
    protected List<User> alumnos = new ArrayList<>();
    @DBRef
    protected List<Horario> horarios = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<User> profesores) {
        this.profesores = profesores;
    }

    public void addProfesor(User user) {
        if (!profesores.stream().map(User::getUsername).toList().contains(user.getUsername())) {
            profesores.add(user);
        }
    }

    public List<User> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<User> alumnos) {
        this.alumnos = alumnos;
    }

    public void addAlumno(User user) {
        if (!alumnos.stream().map(User::getUsername).toList().contains(user.getUsername())) {
            alumnos.add(user);
        }
    }

    public void removeAlumno(String alumnoId) {
        int index = 0;
        for (User user : alumnos) {
            if (Objects.equals(alumnoId, user.getUsername())) {
                break;
            }
            index++;
        }
        alumnos.remove(index);
    }

    public void removeProfesor(String profesorId) {
        int index = 0;
        for (User user : profesores) {
            if (Objects.equals(profesorId, user.getUsername())) {
                break;
            }
            index++;
        }
        profesores.remove(index);
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public boolean addHorario(Horario horario) {
        return horarios.add(horario);
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displaName) {
        this.displayName = displaName;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }
}
