package com.cl.clapp.clapprepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.cl.clapp.model.Employee;
import com.cl.clapp.model.Leave;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,UUID>{
    
   // @Query("select l from leave l where l.emp_code= ?1")
    /**
     * @param empCode
     * @return
     */
    List<Leave> findAllByEmployee(Employee employee);

    @Query(value = "select * from leave where emp_id in ?1",nativeQuery = true)
    List<Leave> findAllByEmpIdIn(List<UUID> empUuids);
    
    @Query(value = "select * from leave where emp_id = ?1 and leave_status <> 'CANCELLED' ",nativeQuery = true)
    List<Leave> findLeavesByEmployeeId(UUID empId);
}
