package com.ramon.provider.rs.entity;

import java.util.ArrayList;
import java.util.List;

public class RSHorarioUsers extends RSHorario {

    protected List<RSUser> profesores = new ArrayList<>();
    protected List<RSUser> alumnos = new ArrayList<>();

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
}


