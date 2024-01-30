package com.cl.clapp.clappcontroller;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cl.clapp.clapprepository.Mockdatarepository;
import com.cl.clapp.clapprepository.PropertiesRepository;
import com.cl.clapp.clappservices.EmployeeService;
import com.cl.clapp.clappservices.LeaveService;
import com.cl.clapp.dto.LeaveInfo;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveAlreadyExistsException;
import com.cl.clapp.model.LeaveStatus;
import com.cl.clapp.model.MockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@RestController
public class ClAppController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private PropertiesRepository propertiesRepository;

    @Autowired
    private Mockdatarepository mockdatarepository;

    ObjectMapper objectMapper = new ObjectMapper();
    /**
     *
     */
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    @PostMapping("/registeremployee")
    public ResponseEntity<String> registerEmployee(@RequestBody String payload)throws Exception{
        Employee employee = new Employee();
        String vResponse  = "";
        HttpStatus status = HttpStatus.valueOf(200);
        Object obj  = JSONValue.parse(payload);
        JSONObject jsonObject = (JSONObject) obj;
        String employeeCode = (String) jsonObject.get("employeeCode");
        String employeeName = (String) jsonObject.get("employeeName");
        String employeeDateOfBirth = (String) jsonObject.get("employeeDateOfBirth");
        int employeeAge = Integer.parseInt((String) jsonObject.get("employeeAge"));
        int clAvailable = Integer.parseInt((String) jsonObject.get("clAvailable"));
        String reportedTo = "";
        if(jsonObject.containsKey("reportedTo")){
            reportedTo = (String)jsonObject.get("reportedTo");   
        }
        System.out.println(employeeAge+""+employeeDateOfBirth+""+employeeCode+""+employeeName);
        Date convertedDate = sdf.parse(employeeDateOfBirth);
        employee.setAge(employeeAge);
        employee.setClAvailable(clAvailable);
        employee.setDateOfBirth(convertedDate);
        employee.setEmpCode(employeeCode);
        employee.setEmployeeName(employeeName);
        if(!reportedTo.equals("")){
            Optional<Employee> manager = employeeService.getEmployeeByEmployeeCode(reportedTo);
            if(manager.isPresent()){
                employee.setReportedTo(manager.get());  
            }
        }
        Employee savedEmployee = employeeService.saveEmployeeToDb(employee);
        if (savedEmployee == null){
            status = HttpStatus.valueOf(404);
            vResponse = "The Error in the saving the employee";
            return ResponseEntity.status(status).body(vResponse);
        }
        status= HttpStatus.valueOf(200);
        objectMapper.setDateFormat(sdf);
        vResponse = objectMapper.writeValueAsString(savedEmployee);
        return ResponseEntity.status(status).body(vResponse);
    }
    @GetMapping("/getemployee")
    public ResponseEntity<String> getEmployee(HttpServletRequest request)  throws Exception{
        Employee employee = null;
        String empCode = request.getParameter("empcode");
        String responseString    = "";
        Optional<Employee> reterivedEmployee    = employeeService.getEmployeeByEmployeeCode(empCode);
        if(reterivedEmployee.isPresent()){
            employee = reterivedEmployee.get();
            objectMapper.setDateFormat(sdf);
            responseString = objectMapper.writeValueAsString(employee);
            return ResponseEntity.ok(responseString);    
        }
        else{
            return ResponseEntity.status(404).body("employee not found...");
        }
   }
    /**
     * @return
     */
    @PostMapping("/applyLeave")
    public ResponseEntity<String> applyLeave(@RequestBody String payload) throws Exception{
        String responseString = "";
        HttpStatus httpStatus = null;
        String employeeId="";
        String fromDate= "";
        String toDate = "";
        String leaveDays= "";
        JSONParser parser = new JSONParser();
        Object object = parser.parse(payload);
        JSONObject jsonObject = (JSONObject) object;
        employeeId = (String) jsonObject.get("employeeId");
        fromDate = (String) jsonObject.get("fromDate");
        toDate = (String) jsonObject.get("toDate");
        leaveDays = (String) jsonObject.get("leaveDays");
        System.out.println("The received data is "+employeeId+"  from Date :"+fromDate+"  to date: "+toDate);
        Date convertedFromDate = sdf2.parse(fromDate);
        Date convertedToDate   = sdf2.parse(toDate);
        Optional<Employee> employee = employeeService.getEmployeeByEmployeeCode(employeeId);
        if(employee.isPresent()){
            Leave leave = new Leave();
            // this <code>employee.get()</code> is method available in Optional's class to reterive the object
            leave.setEmployee(employee.get());
            leave.setFromDate(convertedFromDate);
            leave.setToDate(convertedToDate);
            leave.setNoOfDays(Integer.parseInt(leaveDays));
            leave.setLeaveType("Causual Leave");
            leave.setLeaveStatus(LeaveStatus.NEW);
            try {
                Leave leaveSaved = leaveService.saveLeaveToDb(leave);
                objectMapper.setDateFormat(sdf); 
                responseString = objectMapper.writeValueAsString(leaveSaved);
                httpStatus = HttpStatus.valueOf(201);
            } catch (LeaveAlreadyExistsException leaveAlreadyExistsException) {
                responseString = objectMapper.writeValueAsString(leaveAlreadyExistsException);
                httpStatus = HttpStatus.valueOf(405);
            }
        }
        return ResponseEntity.status(httpStatus).body(responseString);
     }
     @GetMapping("/getmysubordinates")
     public ResponseEntity<String> getMySubordinates (HttpServletRequest request) throws JsonProcessingException{
        Employee employee = null;
        List<?> subordinatesList = null;
        String responseString = "No Subordiantes Under this employee....";
        HttpStatus httpStatus = HttpStatus.valueOf(204);
        String empCode = request.getParameter("empcode");
        Optional<Employee> reterivedEmployee    = employeeService.getEmployeeByEmployeeCode(empCode);
        if(reterivedEmployee.isPresent()){
            employee = reterivedEmployee.get();
            subordinatesList = employeeService.getAllSubordinates(employee.getEmpId().toString());
        }
        if(!subordinatesList.isEmpty()){
            responseString = objectMapper.writeValueAsString(subordinatesList);
            httpStatus = HttpStatus.valueOf(200);
        }
        return ResponseEntity.status(httpStatus).body(responseString);
     }

     /**
     * @param hServletRequest
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/getmysubordinatesleaves")
     public ResponseEntity<String> getSubordinatesLeaves(HttpServletRequest hServletRequest) throws JsonProcessingException{
        String employeecode = hServletRequest.getParameter("empcode");
        Employee employee = null;
        List<UUID> subordinatesList = null;
        String responseString = "No Leaves Found...";
        HttpStatus httpStatus = HttpStatus.valueOf(204);
        Optional<Employee> reterivedEmployee    = employeeService.getEmployeeByEmployeeCode(employeecode);
        System.out.println(reterivedEmployee.get().getEmpCode());
        if(reterivedEmployee.isPresent()){
            employee = reterivedEmployee.get();
            subordinatesList = employeeService.getAllSubordinatesWithOnlyUUID(employee.getEmpId().toString());
        }
        if(!subordinatesList.isEmpty()){
            List<Leave> leaves = employeeService.getSubordinatesLeaves(subordinatesList);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            responseString = objectMapper.writeValueAsString(leaves);
            httpStatus = HttpStatus.valueOf(200);
        }
        return ResponseEntity.status(httpStatus).body(responseString);
     }

     /**
     * @param httpServletRequest
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/getMyLeaves")
     public ResponseEntity<String> getMyLeaves(HttpServletRequest httpServletRequest) throws JsonProcessingException{
        String employeeCode = httpServletRequest.getParameter("empcode");
        String vResponse = "No Leaves Found ...";
        HttpStatus httpStatus = HttpStatus.valueOf(204);
        Optional<Employee> reterivedEmployee = employeeService.getEmployeeByEmployeeCode(employeeCode);
        List<LeaveInfo> leaveList = new ArrayList<>();
        if(reterivedEmployee.isPresent()){
            leaveList = employeeService.findAllEmployeeLeaves(reterivedEmployee.get().getEmpId());
        }
        if(!leaveList.isEmpty()){
            vResponse = objectMapper.writeValueAsString(leaveList);
            httpStatus = HttpStatus.valueOf(200);
        }
        return ResponseEntity.status(httpStatus).body(vResponse);
     }

     @PutMapping("/updateleave")
     public ResponseEntity<String> updateLeaveStatus(@RequestBody String payload) {
        Object obj = JSONValue.parse(payload);
        JSONObject jsonObject = (JSONObject) obj;

        String  vResponse = "";
        HttpStatus httpStatus = HttpStatus.valueOf(200);
        String empId = (String)jsonObject.get("empId");
        String leaveId = (String)jsonObject.get("leaveId");
        String nextLeaveStatus = (String)jsonObject.get("nextStatus");
        try{
            Leave savedLeave = leaveService.updateExistingLeaveStatus(empId,leaveId,nextLeaveStatus);
            System.out.println(savedLeave.toString());
            objectMapper.setDateFormat(sdf);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);    
            vResponse = objectMapper.writeValueAsString(savedLeave);
        }
        catch(Exception exception){
            vResponse = exception.getMessage();
            httpStatus = HttpStatus.valueOf(405);
        }    
        return ResponseEntity.status(httpStatus).body(vResponse);
     }

     @GetMapping(value="/getProperties")
     public String getMethodName() throws JsonProcessingException {
        List<?> holidayList = propertiesRepository.getHolidayDatesListFromProperties();
        

/*
        for(String holiday : (String) holidayList){
            System.out.println("the holiday date is :"+holiday);
        }*/
         return  objectMapper.writeValueAsString(holidayList);
     }

     @GetMapping(value="/calculateLeaveDuration")
     public String calculateLeaveDuration(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String fromString = httpServletRequest.getParameter("from");
        String toString   = httpServletRequest.getParameter("to");
         leaveService.calculateLeaveDurationAfterExcludingHolidays(fromString,toString);
         return  objectMapper.writeValueAsString("Sucess...");
     }

     @GetMapping(value = "/getMockData")
     public String getMockData(HttpServletRequest httpServletRequest) throws JsonProcessingException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fromString = httpServletRequest.getParameter("from");
        String toString   = httpServletRequest.getParameter("to");
        List<MockData> mockdata =   mockdatarepository.findAll();
        List<MockData> fileteredMockData = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(fromString, formatter);
        LocalDate endDate   = LocalDate.parse(toString, formatter);
        System.out.println("localDate area "+startDate);
        System.out.println("the original size of mockdata: "+mockdata.size());
    
      for (MockData mock : mockdata){
            LocalDate fromDate = LocalDate.parse(mock.getFromDate(),formatter);
            LocalDate toDate   = LocalDate.parse(mock.getToDate(),formatter);            
            if ((fromDate.isAfter(startDate) && fromDate.isBefore(endDate))  || (toDate.isBefore(endDate) && toDate.isAfter(startDate))){
                System.out.println("The from data is "+fromDate.toString()+" and to Date is :"+toDate.toString());
                fileteredMockData.add(mock);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(fileteredMockData.size());

        return mapper.writeValueAsString(fileteredMockData);
     }
}
