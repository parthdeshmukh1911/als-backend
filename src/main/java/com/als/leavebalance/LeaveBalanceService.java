package com.als.leavebalance;

import java.util.List;

import com.als.dto.LeaveBalanceDTO;

public interface LeaveBalanceService {
    List<LeaveBalance> getLeaveBalancesByEmployeeId(Long employeeId);
    
    LeaveBalance getLeaveBalanceByEmployeeAndCategory(Long employeeId, Long categoryId);
    
    LeaveBalance updateLeaveBalance(Long employeeId, Long categoryId, int balance);
    
    LeaveBalance createLeaveBalance(Long employeeId, Long categoryId, int balance);
}
