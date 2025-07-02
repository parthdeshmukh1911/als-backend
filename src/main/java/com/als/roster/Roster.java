package com.als.roster;

import java.time.LocalDate;

import com.als.employee.Employee;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roster")
@Data
public class Roster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rosterId;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    
    private LocalDate rosterDate;
    
    private String rosterType;
}

