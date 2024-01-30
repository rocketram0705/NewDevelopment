package com.cl.clapp.clappservices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cl.clapp.clapprepository.EmployeeRepository;
import com.cl.clapp.clapprepository.LeaveRepository;
import com.cl.clapp.clapprepository.PropertiesRepository;
import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;
import com.cl.clapp.model.LeaveAlreadyExistsException;
import com.cl.clapp.model.LeaveStatus;
import com.cl.clapp.model.NumberOfLeavesExceedsAvailableLeavesException;
import com.cl.clapp.model.Property;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveService {
    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PropertiesRepository propertiesRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    SimpleDateFormat  sdf = new SimpleDateFormat("dd/MM/yyyy");

    Logger logger = LoggerFactory.getLogger(LeaveService.class);
    
    /**
     * @param leave
     * @return
     */
    public Leave saveLeaveToDb(Leave leave){
       // System.out.println("the employee code to be as input is " +employeeCode);
        
       Optional<Employee> employee = employeeRepository.findById(leave.getEmployee().getEmpId());

       if(employee.get().getClAvailable() < leave.getNoOfDays()){
        throw new NumberOfLeavesExceedsAvailableLeavesException();
       }
        List<Leave> leaves  = new ArrayList<Leave>();
        leaves=  leaveRepository.findAllByEmployee(leave.getEmployee());

       /* below code will execute if there are leaves present the list.
          the list of old leaves is iterated and each old leave's from date and to date compared against new leave's from date and to date.
        * if current leave overlap with any old leaves the leave already exists exception will be thrown.
          if no old leaves overlap with current leave then the loop excutes all iterations and terminate.
          then then rest of the code is executed to persist the leave in the data base.
        */
        java.sql.Date leaveFromDate =  new java.sql.Date(leave.getFromDate().getTime());
        java.sql.Date leaveToDate =    new java.sql.Date(leave.getToDate().getTime());
        if(!leaves.isEmpty()){
            for(Leave oldLeave :leaves){
                if(!((leaveFromDate.before(oldLeave.getFromDate()) && leaveToDate.before(oldLeave.getFromDate())) || (leaveFromDate.after(oldLeave.getToDate()) && leaveToDate.after(oldLeave.getToDate())))){
                    throw new LeaveAlreadyExistsException();
                }
            }
        }    
        Leave newLeave = leaveRepository.save(leave);
        return newLeave;
    }


    public Leave updateExistingLeaveStatus(String empId, String leaveId, String nextLeaveStatus) throws Exception {
        Optional<Leave> leave = leaveRepository.findById(UUID.fromString(leaveId));
        if(!leave.isPresent()){
            System.out.println("inside exception......");
            throw new RuntimeException("No Leaves Found....");
        }
        Leave foundLeave = leave.get();
        String fromDate   = foundLeave.getFromDate().toString();
        String toDate     = foundLeave.getToDate().toString();  
        LeaveStatus nextLeaveStatus2 = null;
        
        switch(nextLeaveStatus){
            case "FORWARDED" :
                 nextLeaveStatus2 = LeaveStatus.FORWARDED; 
                 break;
            case "APPROVED"  :
                 nextLeaveStatus2 = LeaveStatus.APPROVED;
                 break;
            case "REJECTED"  :
                 nextLeaveStatus2 = LeaveStatus.REJECTED;
                 break;
            case "RESUBMIT"  :
                 nextLeaveStatus2 = LeaveStatus.RESUBMIT;
                 break;
            case "CANCELLED" :
                 nextLeaveStatus2 = LeaveStatus.CANCELLED;
                 break;
            default          :
                 nextLeaveStatus2 = LeaveStatus.NEW;
                 break;
        }
        
        if(foundLeave.getLeaveStatus().equals(LeaveStatus.APPROVED) && nextLeaveStatus2.equals(LeaveStatus.CANCELLED)){
            foundLeave.setLeaveStatus(nextLeaveStatus2);
            Optional<Employee> employee  = employeeRepository.findById(UUID.fromString(empId));
            if(employee.isPresent()){
                Employee emp = employee.get();
                int leaveDuration = calculateLeaveDurationAfterExcludingHolidays(fromDate,toDate);
                emp.setClAvailable(leaveDuration);
                employeeRepository.save(emp);
            } 
            return  leaveRepository.save(foundLeave);  
        }
        else if (nextLeaveStatus2.equals(LeaveStatus.APPROVED)){
            foundLeave.setLeaveStatus(nextLeaveStatus2);
            
            Optional<Employee> employee  = employeeRepository.findById(UUID.fromString(empId));
            if(employee.isPresent()){
                Employee emp = employee.get();

                int leaveDuration = calculateLeaveDurationAfterExcludingHolidays(fromDate,toDate);
                emp.setClAvailable(leaveDuration);
                employeeRepository.save(emp);
            }
            
            return leaveRepository.save(foundLeave);
        }
        else{
            foundLeave.setLeaveStatus(nextLeaveStatus2);
            return leaveRepository.save(foundLeave);
        }
    }
    public int  calculateLeaveDurationAfterExcludingHolidays(String fromDate,String toDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate lFromDate = LocalDate.parse(fromDate,formatter);
        LocalDate lToDate = LocalDate.parse(toDate,formatter);
        int sundayCount = 0;
        int holidayCount = 0;
        Property holidayListObj = propertiesRepository.getHolidaysListFromProperties();
        LinkedHashMap<String,String> holidayListMap  = holidayListObj.getPropertyValue();
        List<LocalDate> nationalHolidayList = new ArrayList<>();
        holidayListMap.forEach((key,value)->{
            nationalHolidayList.add(LocalDate.parse(value, formatter1));
        });
      //  System.out.println(jsonLeaveListObj.toJSONString());
        for(LocalDate i = lFromDate;!i.isAfter(lToDate);i= i.plusDays(1)){
            if(i.getDayOfWeek().toString().equals("SUNDAY")){

                sundayCount++;
            }
            if(nationalHolidayList.contains(i)){
                holidayCount++;                
            }
        }
       
        long totalNoOfDays = lFromDate.until(lToDate,ChronoUnit.DAYS)+1;
        logger.info("Total no leaves:"+totalNoOfDays+" and sunday count:"+sundayCount+" and holiday count :"+holidayCount);
        int calcualatedLeaveDuration = (int) totalNoOfDays - sundayCount - holidayCount;
        logger.info("Total no leaves:"+totalNoOfDays+" and sunday count:"+sundayCount+" and holiday count :"+holidayCount+" the leave duration is :"+calcualatedLeaveDuration);
        return calcualatedLeaveDuration;
    }
}
