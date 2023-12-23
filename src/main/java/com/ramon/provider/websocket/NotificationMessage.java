package com.ramon.provider.websocket;


import java.util.Date;

public class NotificationMessage {

    protected Object value;
    protected String objectType;
    protected Date creationDate;

    public NotificationMessage(Object value, String objectType, Date creationDate) {
        this.value = value;
        this.objectType = objectType;
        this.creationDate = creationDate;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
