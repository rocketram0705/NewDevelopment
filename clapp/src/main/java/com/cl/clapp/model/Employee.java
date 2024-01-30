package com.cl.clapp.model;

import java.util.Date;
//import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
//import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cl.clapp.dto.EmployeeInfo;
import com.cl.clapp.dto.EmployeeUUID;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@NamedNativeQuery(name = "Employee.getEmployeeUuidsNameQuery",
                 query = "SELECT employee_id as employeeId FROM getEmployeesuuid(?1)",
                 resultSetMapping = "Mapping.EmployeeUUID")
@SqlResultSetMapping(name = "Mapping.EmployeeUUID",
                    classes = @ConstructorResult(targetClass = EmployeeUUID.class,
                    columns = {@ColumnResult(name = "employeeId",type = UUID.class)}))

@NamedNativeQuery(name="Employee.getEmployeeInfoNamedQuery",
                  query = "SELECT employee_code as employeeCode,employee_name as employeeName, date_of_birth as dateOfBirth,age as age FROM  getemployeeinfo(?1)",
                  resultSetMapping = "Mapping.EmployeeInfo")
@SqlResultSetMapping(name="Mapping.EmployeeInfo",
                    classes = @ConstructorResult(targetClass = EmployeeInfo.class,columns = {@ColumnResult(name="employeeCode",type = String.class),@ColumnResult(name="employeeName",type = String.class),@ColumnResult(name="dateOfBirth",type = Date.class),@ColumnResult(name="age",type = Integer.class)}))                 
@Entity

//@JsonIgnoreProperties(value = "hibernateLazyInitializer")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID    empId;
    @Column(unique = true,nullable = false)
    private String  empCode;
    private String  employeeName;
    /**
     *
     */
    @Temporal(TemporalType.DATE)
    private Date    dateOfBirth;
    private int     age;
    private int     clAvailable;
    @ManyToOne
    @JoinColumn(name = "reportedTo",unique = false)
    private Employee reportedTo;
  //  @OneToMany(fetch = FetchType.LAZY,mappedBy = "employee")
  //  private List<Leave> leave;
    
 /*   public Employee(UUID empId, String empCode, String employeeName, Date dateOfBirth, int age, int clAvailable,
            Employee reportedTo) {
        this.empId = empId;
        this.empCode = empCode;
        this.employeeName = employeeName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.clAvailable = clAvailable;
        this.reportedTo = reportedTo;
    } */ 
       public Employee getReportedTo() {
        return reportedTo;
    }
    public void setReportedTo(Employee reportedTo) {
        this.reportedTo = reportedTo;
    }
    /**
     * 
     */
    public Employee() {
    }
    public Employee(UUID empId, String empCode, String employeeName, Date dateOfBirth, int age, int clAvailable) {
        this.empId          = empId;
        this.empCode        = empCode;
        this.employeeName   = employeeName;
        this.dateOfBirth    = dateOfBirth;
        this.age            = age;
        this.clAvailable    = clAvailable;
    }
    
   public UUID getEmpId() {
        return empId;
    }
    public void setEmpId(UUID empId) {
        this.empId = empId;
    }
    
    public String getEmpCode() {
        return empCode;
    }
    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }
 /*   public List<Leave> getLeave() {
        return leave;
    }
    public void setLeave(List<Leave> leave) {
        this.leave = leave;
    }*/
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getClAvailable() {
        return clAvailable;
    }
    public void setClAvailable(int clAvailable) {
        this.clAvailable = clAvailable;
    }
       
}
