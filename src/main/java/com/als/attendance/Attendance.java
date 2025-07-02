package com.als.attendance;

import java.time.LocalDate;
import java.time.LocalTime;

import com.als.employee.Employee;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    private LocalDate attendanceDate;
    
    private LocalTime attendanceTime;
    
    private String attendanceStatus;
    
    private String workMode;
}

