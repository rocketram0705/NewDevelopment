package com.cl.clapp;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cl.clapp.clappcontroller.ClAppController;
import com.cl.clapp.clappservices.EmployeeService;
import com.cl.clapp.clappservices.LeaveService;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveStatus;

@WebMvcTest(ClAppController.class)
public class ControllerLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private LeaveService leaveService;
    

    @Test
    public void shouldGetAllEmployeeLeavesFromRepository() throws Exception{
        Employee employee1 = new Employee();
        employee1.setEmpId(UUID.randomUUID());
        employee1.setAge(30);
        employee1.setDateOfBirth(new Date("25/04/1992"));
        employee1.setEmployeeName("RAm");
        employee1.setClAvailable(30);
        employee1.setEmpCode("EMP0005");

        
        List<Leave> leaveList = new ArrayList<>();
        Leave leave1 = new Leave(UUID.randomUUID(),employee1,new Date("25/04/2023"),new Date("26/04/2023"),LeaveStatus.NEW);
        Leave leave2 = new Leave(UUID.randomUUID(),employee1,new Date("28/04/2023"),new Date("28/04/2023"),LeaveStatus.NEW);
        Leave leave3 = new Leave(UUID.randomUUID(),employee1,new Date("22/04/2023"),new Date("23/04/2023"),LeaveStatus.NEW);
        leaveList.add(leave2);
        leaveList.add(leave1);
        leaveList.add(leave3);      
        List<UUID> employeeuuids = new ArrayList<>();
        employeeuuids.add(employee1.getEmpId());
        employeeuuids.add(UUID.fromString("d702c0f0-4440-11ee-a94e-9fdb3e4b4e3f"));
        when(employeeService.getEmployeeByEmployeeCode("EMP0005")).thenReturn(Optional.of(employee1));
        when(employeeService.getAllSubordinatesWithOnlyUUID(employee1.getEmpId().toString())).thenReturn(employeeuuids);
       when(employeeService.getSubordinatesLeaves(employeeuuids)).thenReturn(leaveList);
        mockMvc.perform(MockMvcRequestBuilders.get("/getmysubordinatesleaves?empcode=EMP0005").accept(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().is(200));           
}

    
    @Test
    public void shouldAccessHomePageOfTheWebSite() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.ALL_VALUE)).andExpect(MockMvcResultMatchers.status().is(200));
    }

}