package com.ramon.provider.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("device")
public class Device implements Serializable {

    @Id
    protected String name;
    protected String originCountry;
    protected Date lastModificationDate;
    protected Date creationDate;
    protected String description;
    protected String category;
    protected String customer;
    protected Integer registerSize = 0;
    protected List<DeviceRegister> registros = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DeviceRegister> getRegistros() {
        return registros;
    }

    public void addRegistro(DeviceRegister register) {
        registros.add(register);
        registerSize++;
    }

    public Integer getRegisterSize() {
        return registros.size();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setRegisterSize(Integer registerSize) {
        this.registerSize = registerSize;
    }
}
