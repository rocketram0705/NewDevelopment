package com.cl.clapp.dto;

import java.util.UUID;

public class EmployeeUUID {
    private UUID employeeId ;

    public EmployeeUUID() {
    }
    

    public EmployeeUUID(UUID employeeId) {
        this.employeeId = employeeId;
    }


    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    
}
