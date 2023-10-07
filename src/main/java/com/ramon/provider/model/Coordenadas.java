package com.ramon.provider.model;

import java.io.Serializable;

public class Coordenadas implements Serializable {

    public Coordenadas() {}

    public Coordenadas(String latitud, String longitud, String altitud, String rumbo) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
        this.rumbo = rumbo;
    }

    protected String latitud;
    protected String longitud;
    protected String altitud;
    protected String rumbo;

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getRumbo() {
        return rumbo;
    }

    public void setRumbo(String rumbo) {
        this.rumbo = rumbo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
