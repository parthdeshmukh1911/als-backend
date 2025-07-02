package com.als.leave;

import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployeeId(Long employeeId);
    
    List<Leave> findByStatus(String status);

    List<Leave> findByEmployeeIdAndStatus(Long employeeId, String status);

	List<Leave> findByEmployeeIdAndStartDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("UPDATE Leave l SET l.status = :status WHERE l.id = :leaveId")
    @Transactional
    void updateLeaveStatusById(@Param("leaveId") Long leaveId, @Param("status") String status);

}

