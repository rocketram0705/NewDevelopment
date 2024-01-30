package com.cl.clapp.dto;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.List;

import com.cl.clapp.model.LeaveStatus;

public class LeaveInfo {
    
    private UUID leaveId;
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Temporal(TemporalType.DATE)
    private Date toDate;
    private LeaveStatus leaveStatus;
    private String leaveType;
    private int noOfDays;
    private List<LeaveStatus> nextLeaveStatus;

    
    public LeaveInfo(UUID leaveId, Date fromDate, Date toDate, LeaveStatus leaveStatus, String leaveType, int noOfDays,
            List<LeaveStatus> nextLeaveStatus) {
        this.leaveId = leaveId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveStatus = leaveStatus;
        this.leaveType = leaveType;
        this.noOfDays = noOfDays;
        this.nextLeaveStatus = nextLeaveStatus;
    }

    public LeaveInfo() {
    }

    
    public UUID getLeaveId() {
        return leaveId;
    }
    public void setLeaveId(UUID leaveId) {
        this.leaveId = leaveId;
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
    public int getNoOfDays() {
        return noOfDays;
    }
    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }
    public List<LeaveStatus> getNextLeaveStatus() {
        return nextLeaveStatus;
    }
    public void setNextLeaveStatus(List<LeaveStatus> nextLeaveStatus) {
        this.nextLeaveStatus = nextLeaveStatus;
    }

}
