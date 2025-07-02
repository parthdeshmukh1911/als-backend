package com.als.leavebalance;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.als.employee.Employee;
import com.als.leavecategory.LeaveCategory;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LeaveBalanceServiceImpl(LeaveBalanceRepository leaveBalanceRepository, ModelMapper modelMapper) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LeaveBalance> getLeaveBalancesByEmployeeId(Long employeeId) {
        try {
            List<LeaveBalance> leaveBalances = leaveBalanceRepository.findByEmployeeId(employeeId);
            return leaveBalances;
                   
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve leave balances by employee ID", e);
        }
    }

    @Override
    public LeaveBalance getLeaveBalanceByEmployeeAndCategory(Long employeeId, Long categoryId) {
        try {
            LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeIdAndLeaveCategoryLeaveCategoryId(employeeId, categoryId);
            return leaveBalance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve leave balance by employee ID and category ID", e);
        }
    }

    @Override
    public LeaveBalance updateLeaveBalance(Long employeeId, Long categoryId, int balance) {
        try {
            LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeIdAndLeaveCategoryLeaveCategoryId(employeeId, categoryId);
            if (leaveBalance != null) {
                leaveBalance.setBalance(balance);
                leaveBalance = leaveBalanceRepository.save(leaveBalance);
                return leaveBalance;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update leave balance", e);
        }
    }

    @Override
    public LeaveBalance createLeaveBalance(Long employeeId, Long categoryId, int balance) {
        try {
            Employee employee = new Employee();
            employee.setId(employeeId);

            LeaveCategory leaveCategory = new LeaveCategory();
            leaveCategory.setLeaveCategoryId(categoryId);

            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(employee);
            leaveBalance.setLeaveCategory(leaveCategory);
            leaveBalance.setBalance(balance);

            leaveBalance = leaveBalanceRepository.save(leaveBalance);
            return leaveBalance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create leave balance", e);
        }
    }
}
