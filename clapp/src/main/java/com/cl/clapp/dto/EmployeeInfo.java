package com.cl.clapp.dto;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class EmployeeInfo {
private String employeeCode;
private String employeeName;
@Temporal(TemporalType.DATE)
private Date dateOfBirth;
private int  age;
public EmployeeInfo() {
}
public EmployeeInfo(String employeeCode, String employeeName, Date dateOfBirth, int age) {
    this.employeeCode = employeeCode;
    this.employeeName = employeeName;
    this.dateOfBirth = dateOfBirth;
    this.age = age;
}
public String getEmployeeCode() {
    return employeeCode;
}
public void setEmployeeCode(String employeeCode) {
    this.employeeCode = employeeCode;
}
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


}
