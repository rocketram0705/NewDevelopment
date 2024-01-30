package com.cl.clapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;
import java.util.LinkedHashMap;

import com.cl.clapp.clapprepository.EmployeeRepository;
import com.cl.clapp.clapprepository.LeaveRepository;
import com.cl.clapp.clapprepository.PropertiesRepository;
import com.cl.clapp.clappservices.LeaveService;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveStatus;
import com.cl.clapp.model.LeaveAlreadyExistsException;
import com.cl.clapp.model.NumberOfLeavesExceedsAvailableLeavesException;
import com.cl.clapp.model.Property;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceLayerTest {
    
    @Mock
    LeaveRepository leaveRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    PropertiesRepository  propertiesRepository;

    @InjectMocks
    LeaveService leaveService;

    List<Leave> leaveList = new ArrayList<Leave>();

    Employee employee1;

    @BeforeEach
    public void setup(){

        employee1 = new Employee();
        employee1.setEmpId(UUID.randomUUID());
        employee1.setAge(30);
        employee1.setDateOfBirth(new Date("25/04/1992"));
        employee1.setEmployeeName("RAm");
        employee1.setClAvailable(30);
        employee1.setEmpCode("EMP0005");
        Leave leave1 = new Leave(UUID.randomUUID(),employee1,new Date("25/04/2023"),new Date("26/04/2023"),LeaveStatus.NEW);
        Leave leave2 = new Leave(UUID.randomUUID(),employee1,new Date("28/04/2023"),new Date("28/04/2023"),LeaveStatus.NEW);
        Leave leave3 = new Leave(UUID.randomUUID(),employee1,new Date("22/04/2023"),new Date("23/04/2023"),LeaveStatus.NEW);
        leaveList.add(leave2);
        leaveList.add(leave1);
        leaveList.add(leave3);

    }

    /**
     * 
     */
    @Test
    void newLeaveShouldBeCompleted(){
        Leave leave5 = new Leave(UUID.randomUUID(),employee1,new Date("20/04/2023"),new Date("20/04/2023"),LeaveStatus.NEW);
        when(employeeRepository.findById(employee1.getEmpId())).thenReturn(Optional.of(employee1));
        when(leaveRepository.findAllByEmployee(employee1)).thenReturn(leaveList);
        when(leaveRepository.save(leave5)).thenReturn(leave5);
        Leave newLeave = leaveService.saveLeaveToDb(leave5);
        assertEquals(leave5,newLeave);
    }

    @Test
    void methodShouldThrowLeaveAlreadyExistException(){
        Leave leave4 = new Leave(UUID.randomUUID(),employee1,new Date("25/04/2023"),new Date("25/04/2023"),LeaveStatus.NEW);
        
        when(employeeRepository.findById(employee1.getEmpId())).thenReturn(Optional.of(employee1));

        when(leaveRepository.findAllByEmployee(employee1)).thenReturn(leaveList);
        
        assertThrows(LeaveAlreadyExistsException.class,()-> {
            Leave newLeave = leaveService.saveLeaveToDb(leave4);
        });
    }

    /**
     * 
     */
    @Test
    void methodShouldThrowNumberOfLeavesExceedsAvailableLeaves(){
        Leave leave5 = new Leave(UUID.randomUUID(),employee1,new Date("25/04/2023"),new Date("25/05/2023"),31,"Casual Leave");

        //Optional<Employee> employee = employee1;
        
        when(employeeRepository.findById(employee1.getEmpId())).thenReturn(Optional.of(employee1));
    
        assertThrows(NumberOfLeavesExceedsAvailableLeavesException.class,()-> {
            Leave newLeave = leaveService.saveLeaveToDb(leave5);
        });
    }

    @Test
    void methodShouldReturnLeaveDurationExcludingSundaysAndHolidays(){

        String fromDate = "2024-01-13";
        String toDate   = "2024-01-18";
       // LeaveService leaveService = new LeaveService();
        Property propertyObj = new Property();
        LinkedHashMap<String,String> holidayListMap = new LinkedHashMap<>();
        holidayListMap.put("New Year","01/01/2024");
        holidayListMap.put("Pongal","15/01/2024");
        propertyObj.setId(UUID.randomUUID());
        propertyObj.setPropertyName("holidays");
        propertyObj.setPropertyValue(holidayListMap);
        when(propertiesRepository.getHolidaysListFromProperties()).thenReturn(propertyObj);
        int noOfDaysleave = leaveService.calculateLeaveDurationAfterExcludingHolidays(fromDate, toDate);
        assertEquals(4,noOfDaysleave);
    }

}