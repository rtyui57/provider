package com.ramon.provider.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Date;

@Document(collection = "mock")
public class Post {

    protected String name;
    protected int quantity;
    protected String category;

    protected Date creationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreationDate() {
        return new Date();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
