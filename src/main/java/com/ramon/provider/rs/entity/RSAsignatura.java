package com.ramon.provider.rs.entity;

import java.util.ArrayList;
import java.util.List;

public class RSAsignatura {

    protected String id;
    protected String name;
    protected String displayName;
    protected String grado;
    protected String curso;
    protected int creditos;
    protected List<RSUser> profesores = new ArrayList<>();
    protected List<RSUser> alumnos = new ArrayList<>();
    protected List<RSHorario> horarios = new ArrayList<>();

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<RSUser> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<RSUser> profesores) {
        this.profesores = profesores;
    }

    public List<RSUser> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<RSUser> alumnos) {
        this.alumnos = alumnos;
    }

    public List<RSHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<RSHorario> horarios) {
        this.horarios = horarios;
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

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}
