package com.ramon.provider.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document("deviceRegister")
public class DeviceRegister implements Serializable {

    protected Date fechaRegistro;

    protected Coordenadas coordenadas;

    protected double temperaturaAmbiente;

    protected double temperaturaCorporal;

    protected double ritmoCardiaco;

    protected double kilometrosAcumulados;

    protected double presion;

    protected double oxigenoSangre;

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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
