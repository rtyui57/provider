package com.ramon.provider.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document("horario")
public class Horario {

    public enum AttendantState {
        TO_ATTEND,
        ATTENDED,
        NOT_ATTENDED
    }

    @Id
    private String id;
    private String title;
    private String descripcion;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm", timezone="Europe/Madrid")
    private Date start;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm", timezone="Europe/Madrid")
    private Date end;
    @DBRef
    private Aula aula;
    @DBRef
    private Asignatura asignatura;
    private String color;
    private Map<String, AttendantState> attendants = new HashMap();

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Map<String, AttendantState> getAttendants() {
        return attendants;
    }

    public void setAttendants(Map<String, AttendantState> attendants) {
        this.attendants = attendants;
    }
}
