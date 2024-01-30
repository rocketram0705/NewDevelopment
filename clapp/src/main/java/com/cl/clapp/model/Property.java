package com.cl.clapp.model;

import java.util.LinkedHashMap;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Property {
    
    @Id
    private UUID id;
    private String propertyName;
    private LinkedHashMap<String,String> propertyValue;

    

    public Property() {
    }

    
    public Property(UUID id, String propertyName, LinkedHashMap<String, String> propertyValue) {
        this.id = id;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    public LinkedHashMap<String, String> getPropertyValue() {
        return propertyValue;
    }
    public void setPropertyValue(LinkedHashMap<String, String> propertyValue) {
        this.propertyValue = propertyValue;
    }

    

}
