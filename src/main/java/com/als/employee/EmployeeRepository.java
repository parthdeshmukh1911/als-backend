package com.als.employee;

import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByManagerId(Long managerId);
    Employee findByEmailId(String email);
    List<Employee> findByStatus(String status);
    
    List<Employee> findByEmployeeType(String employeeType);

	List<Employee> findByFirstName(String firstName);
	
	List<Employee> findByManagerIdAndDateOfJoiningBetween(Long managerId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.status = :status WHERE e.id = :employeeId")
    void updateEmployeeStatusById(@Param("employeeId") Long employeeId, @Param("status") String status);

}

