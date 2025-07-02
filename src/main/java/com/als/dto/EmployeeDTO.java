package com.als.dto;

import java.time.LocalDate;

//import com.als.department.Department;
//import com.als.employee.Employee;

import lombok.Data;
@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private LocalDate dateOfJoining;
    private String designation;
    private String jobTitle;
    private Long departmentId;
    private String status;
    private LocalDate dateOfBirth;
    private String country;
    private String maritalStatus;
    private String gender;
    private long contactNumber;
    private long emergencyContactNumber;
    private String permanentAddress;
    private String employeeType;
    private Long managerId;


    // Constructors, getters, and setters
}
