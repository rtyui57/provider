package com.ramon.provider.rs.entity;

import com.ramon.provider.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RSUser {

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
    protected User.PUESTO puesto;
    protected List<RSAsignatura> asignaturas = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User.PUESTO getPuesto() {
        return puesto;
    }

    public void setPuesto(User.PUESTO puesto) {
        this.puesto = puesto;
    }

    public List<RSAsignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<RSAsignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
