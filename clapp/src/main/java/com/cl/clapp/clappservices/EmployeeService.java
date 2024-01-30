package com.cl.clapp.clappservices;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cl.clapp.clapprepository.EmployeeRepository;
import com.cl.clapp.clapprepository.LeaveRepository;
import com.cl.clapp.dto.EmployeeInfo;
import com.cl.clapp.dto.EmployeeUUID;
import com.cl.clapp.dto.LeaveInfo;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveStatus;


@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired 
    private LeaveRepository leaveRepository;



    public Employee saveEmployeeToDb(Employee employee){
        Optional<Employee> existingEmployee = employeeRepository.findByEmpCode(employee.getEmpCode());
        if(existingEmployee.isPresent()){
            System.out.println("The employee is already present...");
            return null;  
        }
        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println(savedEmployee.toString());
        return savedEmployee;
    }

    public Optional<Employee> getEmployeeByEmployeeCode(String employeeCode){
        return employeeRepository.findByEmpCode(employeeCode);
    }

    public Optional<Employee> findEmployeeWithTheEmpId(String empId){
        Optional<Employee> employee = employeeRepository.findById(UUID.fromString(empId));
        return employee;
    }
    
    public List<EmployeeInfo> getAllSubordinates(String employeeId) {
         List<EmployeeInfo> employeeInfo = employeeRepository.getEmployeeInfoNamedQuery(UUID.fromString(employeeId));
         return employeeInfo;
    }

    public List<UUID> getAllSubordinatesWithOnlyUUID(String employeeId) {
        List<EmployeeUUID> employeeUUIDs = employeeRepository.getEmployeeUuidsNameQuery(UUID.fromString(employeeId));
        List<UUID> employeeUuids2 = new ArrayList<UUID>();
        if(!employeeUUIDs.isEmpty()){
            for(EmployeeUUID employee : employeeUUIDs){
                employeeUuids2.add(employee.getEmployeeId());
            }
        }
        return employeeUuids2;
    }

    public List<Leave> getSubordinatesLeaves(List<UUID> subordinatesList) {
        return leaveRepository.findAllByEmpIdIn(subordinatesList);
    }

    public List<LeaveInfo> findAllEmployeeLeaves(UUID empId) {
        List<Leave> leaveList = leaveRepository.findLeavesByEmployeeId(empId);

        List<LeaveInfo> leaveInfoList = new ArrayList<>();

        for(Leave leave : leaveList){
            LeaveInfo leaveInfo = new LeaveInfo();
            leaveInfo.setLeaveId(leave.getLeaveId());
            leaveInfo.setFromDate(leave.getFromDate());
            leaveInfo.setToDate(leave.getToDate());
            leaveInfo.setLeaveType(leave.getLeaveType());
            leaveInfo.setNoOfDays(leave.getNoOfDays());
            leaveInfo.setLeaveStatus(leave.getLeaveStatus());

            if(leave.getLeaveStatus().equals(LeaveStatus.NEW) || leave.getLeaveStatus().equals(LeaveStatus.APPROVED)){
                leaveInfo.setNextLeaveStatus(Arrays.asList(LeaveStatus.CANCELLED));
            }
            if(leave.getLeaveStatus().equals(LeaveStatus.REJECTED)){
                leaveInfo.setNextLeaveStatus(Arrays.asList(LeaveStatus.RESUBMIT)); 
            }
            if(leave.getLeaveStatus().equals(LeaveStatus.FORWARDED)){
                leaveInfo.setNextLeaveStatus(Arrays.asList(LeaveStatus.APPROVED,LeaveStatus.REJECTED)); 
            }
            leaveInfoList.add(leaveInfo);
        }

        return leaveInfoList;
    }  
}
