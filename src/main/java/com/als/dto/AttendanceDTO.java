package com.als.dto;


import java.time.LocalDate;
import java.time.LocalTime;



import lombok.Data;

@Data
public class AttendanceDTO {
    private Long attendanceId;
    private Long employeeId;
    private LocalDate attendanceDate;
    private LocalTime attendanceTime;
    private String attendanceStatus;
    private String workMode;
    
    // Constructors, getters, and setters
}
