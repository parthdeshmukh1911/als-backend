package com.als.leave;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.als.dto.LeaveDTO;

@RestController
@RequestMapping("/leaves")
@CrossOrigin
public class LeaveController {
    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Leave>> getEmployeeLeaves(
            @PathVariable Long employeeId,
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate
    ) {
        try {
            List<Leave> leaves = leaveService.getEmployeeLeaves(employeeId, startDate, endDate);
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Leave>> getLeavesByStatus(@PathVariable String status) {
        try {
            List<Leave> leaves = leaveService.getLeavesByStatus(status);
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{leaveId}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long leaveId) {
        try {
            leaveService.deleteLeave(leaveId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Leave>> getAllLeavesByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<Leave> leaves = leaveService.getAllLeavesByEmployeeId(employeeId);
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<Leave> applyLeave(@RequestBody LeaveDTO leaveDTO) {
        try {
            Leave createdLeave = leaveService.applyLeave(leaveDTO);
            return new ResponseEntity<>(createdLeave, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateLeave(@RequestParam(name = "leaveId") Long leaveId , @RequestParam(name = "status") String status) {
        try {
            leaveService.updateLeave(leaveId , status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Leave>> getLeaveByManagerId(@PathVariable Long managerId) {
        try {
            List<Leave> leaves = leaveService.getLeaveByManagerId(managerId);
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        } catch (LeaveException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
