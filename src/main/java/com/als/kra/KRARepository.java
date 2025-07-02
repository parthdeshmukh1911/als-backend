package com.als.kra;

import com.als.kra.KRA;
import com.als.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KRARepository extends JpaRepository<KRA, Long> {
    List<KRA> findByEmployee(Employee employee);

    List<KRA> findByEmployeeId(Long employeeId);

    KRA findByIdAndEmployeeId(Long kraId, Long employeeId);

    boolean existsByEmployeeIdAndFinancialYear(Long employeeId, String financialYear);
}
