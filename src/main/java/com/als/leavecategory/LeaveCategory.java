package com.als.leavecategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "leave_category")
@Data
public class LeaveCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveCategoryId;
    
    private String leaveCategoryName;

}
