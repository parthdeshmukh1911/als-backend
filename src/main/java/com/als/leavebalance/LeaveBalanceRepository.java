package com.als.leavebalance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByEmployeeId(Long employeeId);
    
    LeaveBalance findByEmployeeIdAndLeaveCategoryLeaveCategoryId(Long employeeId, Long categoryId);
}

