package com.als.dto;



import java.time.LocalDate;



import lombok.Data;

@Data
public class LeaveDTO {
    private Long employeeId;
    private Long leaveCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    
    // Constructors, getters, and setters
}
