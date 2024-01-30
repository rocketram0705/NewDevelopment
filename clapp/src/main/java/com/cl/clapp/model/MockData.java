package com.cl.clapp.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MockData {

    @Id
    private UUID id;

    private String fromDate;
    private String toDate;
    private int noOfDays;

    
    public MockData() {
    }

    
    public MockData(UUID id, String fromDate, String toDate, int noOfDays) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.noOfDays = noOfDays;
    }


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getFromDate() {
        return fromDate;
    }
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public String getToDate() {
        return toDate;
    }
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    public int getNoOfDays() {
        return noOfDays;
    }
    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    
}
