package com.cl.clapp.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
//@JsonIgnoreProperties(value = "hibernateLazyInitializer")
public class Leave {
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private UUID    leaveId;

    @Temporal(TemporalType.DATE)
    private Date    fromDate;

    @Temporal(TemporalType.DATE)
    private Date    toDate;

    private int     noOfDays;

    private String  leaveType;

    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id",nullable = false)
    private Employee employee;

    public Leave() {
        this.leaveStatus = LeaveStatus.NEW;
    }

    public Leave(UUID leaveId, Employee employee, Date fromDate, Date toDate, int noOfDays,String leaveType) {
        this.leaveId = leaveId;
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.noOfDays = noOfDays;
        this.leaveStatus =LeaveStatus.NEW;
        this.leaveType = leaveType;
    }
    
    public Leave(UUID leaveId, Employee employee, Date fromDate, Date toDate, LeaveStatus leaveStatus) {
        this.leaveId = leaveId;
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveStatus = leaveStatus;
    }
    public UUID getLeaveId() {
        return leaveId;
    }
    public void setLeaveId(UUID leaveId) {
        this.leaveId = leaveId;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Date getToDate() {
        return toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    public int getNoOfDays() {
        return noOfDays;
    }
    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }
    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }
    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public String getLeaveType() {
        return leaveType;
    }
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return this.leaveType +""+this.fromDate +""+this.toDate +""+this.noOfDays +""+this.employee;
    } 
}
