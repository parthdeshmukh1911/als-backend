package com.als.leavecategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveCategoryServiceImpl implements LeaveCategoryService {

    private final LeaveCategoryRepository leaveCategoryRepository;

    @Autowired
    public LeaveCategoryServiceImpl(LeaveCategoryRepository leaveCategoryRepository) {
        this.leaveCategoryRepository = leaveCategoryRepository;
    }

    @Override
    public List<LeaveCategory> getAllLeaveCategories() {
        return leaveCategoryRepository.findAll();
    }

    @Override
    public LeaveCategory getLeaveCategoryById(Long leaveCategoryId) {
        Optional<LeaveCategory> optionalLeaveCategory = leaveCategoryRepository.findById(leaveCategoryId);
        return optionalLeaveCategory.orElse(null);
    }

    @Override
    public LeaveCategory createLeaveCategory(LeaveCategory leaveCategory) {
        return leaveCategoryRepository.save(leaveCategory);
    }

    @Override
    public LeaveCategory updateLeaveCategory(Long leaveCategoryId, LeaveCategory leaveCategory) {
        LeaveCategory existingLeaveCategory = getLeaveCategoryById(leaveCategoryId);
        if (existingLeaveCategory != null) {
            existingLeaveCategory.setLeaveCategoryName(leaveCategory.getLeaveCategoryName());
            return leaveCategoryRepository.save(existingLeaveCategory);
        }
        return null;
    }

    @Override
    public void deleteLeaveCategory(Long leaveCategoryId) {
        leaveCategoryRepository.deleteById(leaveCategoryId);
    }
}
