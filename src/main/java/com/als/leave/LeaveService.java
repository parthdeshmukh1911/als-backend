package com.als.leave;

import java.time.LocalDate;
import java.util.List;

import com.als.dto.LeaveDTO;

public interface LeaveService {
    List<Leave> getEmployeeLeaves(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    List<Leave> getLeavesByStatus(String status);
    
    void updateLeave(Long leaveId , String status);
    
    Leave getLeaveById(Long leaveId);
    
    void deleteLeave(Long leaveId);

    List<Leave> getAllLeavesByEmployeeId(Long employeeId);

    Leave applyLeave(LeaveDTO leave);

    List<Leave> getLeaveByManagerId(Long managerId);
}

