package com.cl.clapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cl.clapp.clapprepository.EmployeeRepository;
import com.cl.clapp.clapprepository.LeaveRepository;
import com.cl.clapp.clapprepository.PropertiesRepository;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveStatus;
import com.cl.clapp.model.Property;
import com.cl.clapp.dto.EmployeeInfo;
import com.cl.clapp.dto.EmployeeUUID;

@SpringBootTest
class ClappApplicationTests {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LeaveRepository leaveRepository;

	@Autowired
	private PropertiesRepository propertiesRepository;
	@Test
	void checkIfPersisting() {
		//given 
		Employee employeeOne = new Employee();
		employeeOne.setAge(30);
		employeeOne.setDateOfBirth(new java.util.Date("20/05/1993"));
		employeeOne.setEmpCode("EMP03030");
		employeeOne.setEmployeeName("Rahul Ravindra");
		employeeOne.setClAvailable(23);
		employeeRepository.save(employeeOne);
		//when
		Employee employee = new Employee();
		employee.setAge(30);
		employee.setEmpCode("EMP0989");
		employee.setClAvailable(32);
		employee.setReportedTo(employeeOne);
		employee.setEmployeeName(" Ram Kholi");
		employee.setDateOfBirth(new java.util.Date("10/09/2000"));

		Employee saveEmployee = employeeRepository.save(employee);
		Employee emp = saveEmployee.getReportedTo();

		// assert
	   // UUID employeeID = emp.getEmpId();

		assertEquals(emp,employeeOne);
	}

	@Test
	void checkIfTheEmployeeInfo(){
		/*Here I have encountered an exception that says ConverterNotFound No capable coverter which will convert the 
		 * tuple or resultset that return from database function
		 * Converter Not Found Exception......
		 * 
		 * This is because ,
		 * in the repostitory , The Class type is different
		*/
		List<EmployeeInfo> empInfo = employeeRepository.getEmployeeInfoNamedQuery(UUID.fromString("d702c0f0-4440-11ee-a94e-9fdb3e4b4e3f"));
		System.out.println(empInfo.size());
		assertEquals(empInfo.size(),28);
	}

	@Test
	void checkIfTheEmployeeInfoProcedureReturnsEmpty(){
		List<EmployeeInfo> employeeInfos = employeeRepository.getEmployeeInfoNamedQuery(UUID.fromString("24ec295c-16db-4d7d-a43a-2a2749341526"));
		assertTrue(employeeInfos.isEmpty());

	}
	
	@Test
	void shouldReturnAllLeavesByEmployee(){
		Employee employee1 = new Employee();
        employee1.setEmpId(UUID.fromString("24ec295c-16db-4d7d-a43a-2a2749341526"));
		List<Leave> leaves = leaveRepository.findAllByEmployee(employee1);
		UUID empIdResult = leaves.get(0).getEmployee().getEmpId();
		assertEquals(employee1.getEmpId(),empIdResult);
	}

	@Test
	void checkIFLeavePersists(){
		Employee employee1 = new Employee();
        employee1.setEmpId(UUID.fromString("24ec295c-16db-4d7d-a43a-2a2749341526"));
        Leave leave1 = new Leave();
		leave1.setEmployee(employee1);
		leave1.setLeaveType("Casual Leave");
		leave1.setFromDate(new java.util.Date("20/05/2023"));
		leave1.setToDate(new java.util.Date("20/05/2023"));
		leave1.setLeaveStatus(LeaveStatus.NEW);
		leave1.setNoOfDays(1);
		Leave leaveSaved = leaveRepository.save(leave1);
		assertEquals(leave1.getFromDate(), leaveSaved.getFromDate());
	}

	@Test 
	void checkIFGetEmployeesUuids(){
		List<EmployeeUUID> empInfo = employeeRepository.getEmployeeUuidsNameQuery(UUID.fromString("d702c0f0-4440-11ee-a94e-9fdb3e4b4e3f"));
		assertEquals(empInfo.size(),28);
	}

	@Test
	void shouldGetAllLeavesOfSubordinates(){
		
		List <UUID> subordinatesList = Arrays.asList(UUID.fromString("d702c0f0-4440-11ee-a94e-9fdb3e4b4e3f"),UUID.fromString("24ec295c-16db-4d7d-a43a-2a2749341526"));
		List<Leave> leaves = leaveRepository.findAllByEmpIdIn(subordinatesList);
		assertEquals(leaves.size(),4);
	}

	@Test
	void shouldGetAllLeavesOfEmployeeFromRepository(){
		List<Leave> leaveList = leaveRepository.findLeavesByEmployeeId(UUID.fromString("24ec295c-16db-4d7d-a43a-2a2749341526"));
		assertEquals(3,leaveList.size());
	}

	@Test
	void shouldReturnJsOnObject(){
		Property property = propertiesRepository.getHolidaysListFromProperties();
		assertNotNull(property);
	}
}