package com.ramon.provider.rs.entity;

import java.util.ArrayList;
import java.util.List;

public class RSAula {

    protected String id;
    protected String name;
    protected int capacity;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<RSHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<RSHorario> horarios) {
        this.horarios = horarios;
    }
}
