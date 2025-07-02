package com.als.leavebalance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.als.dto.LeaveBalanceDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/leavebalances")
@CrossOrigin
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;

    @Autowired
    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<LeaveBalance>> getLeaveBalancesByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<LeaveBalance> leaveBalances = leaveBalanceService.getLeaveBalancesByEmployeeId(employeeId);
            return new ResponseEntity<>(leaveBalances, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{employeeId}/{categoryId}")
    public ResponseEntity<LeaveBalance> getLeaveBalanceByEmployeeAndCategory(
            @PathVariable Long employeeId,
            @PathVariable Long categoryId
    ) {
        try {
            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceByEmployeeAndCategory(employeeId, categoryId);
            if (leaveBalance != null) {
                return new ResponseEntity<>(leaveBalance, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{employeeId}/{categoryId}")
    public ResponseEntity<LeaveBalance> updateLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable Long categoryId,
            @RequestParam(name = "balance") int balance
    ) {
        try {
            LeaveBalance updatedLeaveBalance = leaveBalanceService.updateLeaveBalance(employeeId, categoryId, balance);
            if (updatedLeaveBalance != null) {
                return new ResponseEntity<>(updatedLeaveBalance, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{employeeId}/{categoryId}")
    public ResponseEntity<LeaveBalance> createLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable Long categoryId,
            @RequestParam(name = "balance") int balance
    ) {
        try {
            LeaveBalance createdLeaveBalance = leaveBalanceService.createLeaveBalance(employeeId, categoryId, balance);
            return new ResponseEntity<>(createdLeaveBalance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
