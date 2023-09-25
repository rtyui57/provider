package com.ramon.provider.rs.entity;

import com.ramon.provider.model.Coordenadas;

public class RSDeviceRegister {

    protected String id;
    protected long timestamp;
    protected Coordenadas coordenadas;
    protected double temperaturaAmbiente;
    protected double temperaturaCorporal;
    protected double ritmoCardiaco;
    protected double kilometrosAcumulados;
    protected double presion;
    protected double oxigenoSangre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Coordenadas getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Coordenadas coordenadas) {
        this.coordenadas = coordenadas;
    }

    public double getTemperaturaAmbiente() {
        return temperaturaAmbiente;
    }

    public void setTemperaturaAmbiente(double temperaturaAmbiente) {
        this.temperaturaAmbiente = temperaturaAmbiente;
    }

    public double getTemperaturaCorporal() {
        return temperaturaCorporal;
    }

    public void setTemperaturaCorporal(double temperaturaCorporal) {
        this.temperaturaCorporal = temperaturaCorporal;
    }

    public double getRitmoCardiaco() {
        return ritmoCardiaco;
    }

    public void setRitmoCardiaco(double ritmoCardiaco) {
        this.ritmoCardiaco = ritmoCardiaco;
    }

    public double getKilometrosAcumulados() {
        return kilometrosAcumulados;
    }

    public void setKilometrosAcumulados(double kilometrosAcumulados) {
        this.kilometrosAcumulados = kilometrosAcumulados;
    }

    public double getPresion() {
        return presion;
    }

    public void setPresion(double presion) {
        this.presion = presion;
    }

    public double getOxigenoSangre() {
        return oxigenoSangre;
    }

    public void setOxigenoSangre(double oxigenoSangre) {
        this.oxigenoSangre = oxigenoSangre;
    }
}
