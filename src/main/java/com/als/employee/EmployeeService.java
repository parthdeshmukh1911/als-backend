package com.als.employee;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.als.dto.EmployeeDTO;


public interface EmployeeService {
    List<Employee> getEmployeesByManagerId(Long managerId);

    List<Employee> getEmployeesByStatus(String status);

    Employee getEmployeeById(Long employeeId);

    Employee getEmployeeByEmailId(String email);

    Employee updateEmployee(EmployeeDTO employee);
    
    Employee updateEmployees(Employee employee);

    void updateEmployeeStatusById(Long employeeId);

    Employee saveEmployee(EmployeeDTO employee);

    void deleteEmployee(Long employeeId);

    List<Employee> getHybridEmployees();

    List<Employee> getVirtualEmployees();

    Employee getEmployeeByName(String name);

    List<Employee> findAll();
    
    void saveExcelFile(MultipartFile file);
    
    ByteArrayInputStream getExcelFileForAllTheEmployees();
    
    ByteArrayInputStream getExcelFileForEmployeesUnderManager(Long id);
    
    List<Employee> getEmployeesByManagerIdAndDateOfJoiningRange(Long managerId, LocalDate startDate, LocalDate endDate);
    
    List<Employee> getAllEmployees() ;
}
