package com.ramon.provider.rs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class RSHorario {

    private String id;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm", timezone="Europe/Madrid")
    private Date start;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm", timezone="Europe/Madrid")
    private Date end;
    private String aula;
    private String asignatura;
    private String title;
    private String color;
    private String descripcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
