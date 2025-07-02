package com.als.leavecategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-categories")
@CrossOrigin
public class LeaveCategoryController {

    private final LeaveCategoryService leaveCategoryService;

    @Autowired
    public LeaveCategoryController(LeaveCategoryService leaveCategoryService) {
        this.leaveCategoryService = leaveCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<LeaveCategory>> getAllLeaveCategories() {
        List<LeaveCategory> leaveCategories = leaveCategoryService.getAllLeaveCategories();
        return ResponseEntity.ok(leaveCategories);
    }

    @GetMapping("/{leaveCategoryId}")
    public ResponseEntity<LeaveCategory> getLeaveCategoryById(@PathVariable Long leaveCategoryId) {
        LeaveCategory leaveCategory = leaveCategoryService.getLeaveCategoryById(leaveCategoryId);
        if (leaveCategory != null) {
            return ResponseEntity.ok(leaveCategory);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<LeaveCategory> createLeaveCategory(@RequestBody LeaveCategory leaveCategory) {
        LeaveCategory createdLeaveCategory = leaveCategoryService.createLeaveCategory(leaveCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLeaveCategory);
    }

    @PutMapping("/{leaveCategoryId}")
    public ResponseEntity<LeaveCategory> updateLeaveCategory(
            @PathVariable Long leaveCategoryId,
            @RequestBody LeaveCategory leaveCategory
    ) {
        LeaveCategory updatedLeaveCategory = leaveCategoryService.updateLeaveCategory(leaveCategoryId, leaveCategory);
        if (updatedLeaveCategory != null) {
            return ResponseEntity.ok(updatedLeaveCategory);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{leaveCategoryId}")
    public ResponseEntity<Void> deleteLeaveCategory(@PathVariable Long leaveCategoryId) {
        leaveCategoryService.deleteLeaveCategory(leaveCategoryId);
        return ResponseEntity.noContent().build();
    }
}
