package com.als.leavebalance;

import com.als.employee.Employee;
import com.als.leavecategory.LeaveCategory;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leave_balance")
@Data
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveBalanceId;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "leave_category_id")
    private LeaveCategory leaveCategory;
    
    private int balance;
}
