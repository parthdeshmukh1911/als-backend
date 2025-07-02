package com.als.leave;

import java.time.LocalDate;

import com.als.employee.Employee;
import com.als.leavecategory.LeaveCategory;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leave_table") // Renamed table to avoid reserved keyword conflict
@Data
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;
    
    @ManyToOne
    @JoinColumn(name = "employee_id" ) // Specified the referenced column name
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_category_id")
    private LeaveCategory leaveCategory;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String reason;
    
    private String status;
    
    
    
//    referencedColumnName = "employee_id"
}


