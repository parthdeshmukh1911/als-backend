package com.als.department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department getDepartmentById(Long departmentId);
    List<Department> getAllDepartments();
    Department updateDepartment(Long departmentId, Department department);
    void deleteDepartment(Long departmentId);
}
