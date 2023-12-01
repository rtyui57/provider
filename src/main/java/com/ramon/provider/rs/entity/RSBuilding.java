package com.ramon.provider.rs.entity;

import java.util.ArrayList;
import java.util.List;

public class RSBuilding {

    protected String id;
    protected String name;
    protected String location;
    protected List<RSAula> aulas = new ArrayList<>();

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<RSAula> getAulas() {
        return aulas;
    }

    public void setAulas(List<RSAula> aulas) {
        this.aulas = aulas;
    }
}
