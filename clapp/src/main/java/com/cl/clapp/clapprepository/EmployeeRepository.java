package com.cl.clapp.clapprepository;

import java.util.Optional;
import java.util.UUID;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cl.clapp.dto.EmployeeInfo;
import com.cl.clapp.dto.EmployeeUUID;
import com.cl.clapp.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,UUID>{

    /**
     * @param empCode
     * @return
     */
    @Query(value ="SELECT * FROM employee e where e.emp_code=:empCode",nativeQuery = true)
    Optional<Employee> findByEmpCode(String empCode);
    /**
     * @param empId
     * @return
     * Below code will throw converternotfoundexception since we specified return type as Employee in the jpaRepository <Employee,UUID>...
     * solution We have to convert them maually 
     */
    @Query(nativeQuery = true)
    List<EmployeeInfo> getEmployeeInfoNamedQuery(UUID empUuid);

    @Query(nativeQuery = true)
    List<EmployeeUUID> getEmployeeUuidsNameQuery(UUID empUuid);
}