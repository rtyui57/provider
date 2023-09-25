package com.ramon.provider.rabbitmq.config;

import java.util.Date;

public class RabbitEvent {

    protected String source;
    protected String key;
    protected Date timestamp;
    protected String user;
    protected String service;
    protected String type;
    protected String host;
    protected RabbitAction action;
    protected Object object;

    public RabbitEvent() {
        this.timestamp = new Date(System.currentTimeMillis());
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RabbitAction getAction() {
        return action;
    }

    public void setAction(RabbitAction action) {
        this.action = action;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
