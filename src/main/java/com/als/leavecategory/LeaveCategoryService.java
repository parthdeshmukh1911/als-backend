package com.als.leavecategory;

import java.util.List;

public interface LeaveCategoryService {
    List<LeaveCategory> getAllLeaveCategories();
    LeaveCategory getLeaveCategoryById(Long leaveCategoryId);
    LeaveCategory createLeaveCategory(LeaveCategory leaveCategory);
    LeaveCategory updateLeaveCategory(Long leaveCategoryId, LeaveCategory leaveCategory);
    void deleteLeaveCategory(Long leaveCategoryId);
}
