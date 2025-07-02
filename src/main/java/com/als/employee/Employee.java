package com.als.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.als.department.Department;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false,unique = true)
    private String emailId;

    @Column(nullable = false)
    private LocalDate dateOfJoining;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String maritalStatus;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private long contactNumber;

    @Column(nullable = false)
    private long emergencyContactNumber;

    @Column(nullable = false)
    private String permanentAddress;

    @Column(nullable = false)
    private String employeeType;
    
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
   // @JsonIgnoreProperties("manager") // Ignore the "manager" property during serialization
    @JsonIgnore
    private Employee manager;
}
