package com.cl.clapp;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cl.clapp.clapprepository.EmployeeRepository;
import com.cl.clapp.clapprepository.LeaveRepository;
import com.cl.clapp.clappservices.EmployeeService;
import com.cl.clapp.dto.EmployeeInfo;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveStatus;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceLayerTest {
    
    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    LeaveRepository leaveRepository;

    @InjectMocks
    EmployeeService employeeService;

    Employee employee1;
    EmployeeInfo employee2;
    EmployeeInfo employee3;

    @BeforeEach
    void setupData(){
        employee1 = new Employee();
        employee1.setEmpId(UUID.randomUUID());
        employee1.setAge(30);
        employee1.setDateOfBirth(new Date("25/04/1992"));
        employee1.setEmployeeName("RAm");
        employee1.setClAvailable(30);
        employee1.setEmpCode("EMP0005");



        employee2 = new EmployeeInfo();
        employee2.setAge(30);
        employee2.setDateOfBirth(new Date("07/05/1993"));
        employee2.setEmployeeCode("EMP0002");
        employee2.setEmployeeName("Rajesh");

        
        employee3 = new EmployeeInfo();
        employee3.setAge(30);
        employee3.setDateOfBirth(new Date("07/05/1993"));
        employee3.setEmployeeCode("EMP0005");
        employee3.setEmployeeName("Ramesh");

    }
    /**
     * 
     */
    @Test
    void testEmployeeServiceReturnSubordinates(){
       
       List<EmployeeInfo> employeeSubordinates= new ArrayList<EmployeeInfo>();
       employeeSubordinates.add(employee2);
       employeeSubordinates.add(employee3);
       when(employeeRepository.getEmployeeInfoNamedQuery(employee1.getEmpId())).thenReturn(employeeSubordinates);

       List<EmployeeInfo> subordianatesList = employeeService.getAllSubordinates(employee1.getEmpId().toString());
        
       assertEquals(subordianatesList.contains(employee3),employeeSubordinates.contains(employee3));
       assertEquals(subordianatesList.size(),employeeSubordinates.size());        
    }
    @Test
    void testEmployeeServiceReturnEmptyList(){

        List<EmployeeInfo> employeeSubordinates= new ArrayList<EmployeeInfo>();
       when(employeeRepository.getEmployeeInfoNamedQuery(employee1.getEmpId())).thenReturn(employeeSubordinates);

       List<EmployeeInfo> subordianatesList = employeeService.getAllSubordinates(employee1.getEmpId().toString());
        
       assertTrue(subordianatesList.isEmpty());
       assertEquals(subordianatesList.size(),0);    

    }
    /**
     * 
     */
    @Test
    void testFindAllLeavesByEmpIdWorksCorrectly(){
        Leave leaves1 = new Leave();
        leaves1.setFromDate(new Date("25/04/2023"));
        leaves1.setToDate(new Date("26/04/2023"));
        leaves1.setLeaveStatus(LeaveStatus.NEW);
        leaves1.setNoOfDays(2);
        leaves1.setLeaveId(UUID.randomUUID());
        leaves1.setEmployee(employee1);

        Leave leaves2  = new Leave();
        leaves2.setEmployee(employee1);
        leaves2.setFromDate(new Date("28/04/2023"));
        leaves2.setToDate(new Date("29/04/2023"));
        leaves2.setNoOfDays(2);
        leaves2.setLeaveStatus(LeaveStatus.NEW);
        leaves2.setLeaveId(UUID.randomUUID());


        Employee employee5 = new Employee();
        employee5.setEmpId(UUID.randomUUID());
        employee5.setAge(30);
        employee5.setDateOfBirth(new Date("25/04/1992"));
        employee5.setEmployeeName("Rajesh");
        employee5.setClAvailable(30);
        employee5.setEmpCode("EMP0005");

        Leave leaves3 = new Leave();
        leaves3.setEmployee(employee5);
        leaves3.setFromDate(new Date("29/05/2023"));
        leaves3.setToDate(new Date("29/05/2023"));
        leaves3.setLeaveId(UUID.randomUUID());
        leaves3.setLeaveStatus(LeaveStatus.NEW);
        leaves3.setNoOfDays(1);

        List <Leave> leaves = new ArrayList<>();
        leaves.add(leaves3);
        leaves.add(leaves2);
        leaves.add(leaves1);

        List<UUID> employeeUuids = new ArrayList<>();
        employeeUuids.add(employee1.getEmpId());
        employeeUuids.add(employee5.getEmpId());

        when(leaveRepository.findAllByEmpIdIn(employeeUuids)).thenReturn(leaves);

        List <Leave> subordinatesLeaves = employeeService.getSubordinatesLeaves(employeeUuids);
        assertEquals(leaves.size(),subordinatesLeaves.size());
    }

}
