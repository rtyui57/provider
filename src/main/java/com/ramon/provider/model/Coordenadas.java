package com.ramon.provider.model;

public class Coordenadas {

    public Coordenadas() {}

    public Coordenadas(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    protected String latitud;
    protected String longitud;

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
