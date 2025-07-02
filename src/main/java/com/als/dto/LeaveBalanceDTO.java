package com.als.dto;






import lombok.Data;

@Data
public class LeaveBalanceDTO {
    private Long leaveBalanceId;
    private Long employeeId;
    private Long leaveCategory;
    private int balance;
    
    // Constructors, getters, and setters
}
