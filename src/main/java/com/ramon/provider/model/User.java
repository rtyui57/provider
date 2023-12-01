package com.ramon.provider.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("user")
@JsonIgnoreProperties({"asignaturas"})
public class User {

    @Id
    protected String username;
    protected String id;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected Date creationDate;
    protected Date modificationDate;
    protected String description;
    protected String icon;
    protected PUESTO puesto;
    @DBRef
    protected List<Asignatura> asignaturas = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public PUESTO getPuesto() {
        return puesto;
    }

    public void setPuesto(PUESTO puestoDado) {
        this.puesto = puestoDado;
    }

    public enum PUESTO {
        PROFESOR,
        ESTUDIANTE,
        CONSERJE
    }

    public String getId() {
        return username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public List<Horario> getHorarios() {
        List<Horario> horarios = new ArrayList<>();
        asignaturas.forEach(a -> horarios.addAll(a.getHorarios()));
        return horarios;
    }
}
