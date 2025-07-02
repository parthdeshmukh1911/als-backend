package com.als.goal;

import com.als.dto.GoalDTO;

import java.util.List;

public interface GoalService {
    GoalDTO saveGoal(GoalDTO goalDTO);
    GoalDTO updateGoal(GoalDTO goalDTO);
    void deleteGoal(Long goalId);
    GoalDTO getGoalById(Long goalId);
    List<GoalDTO> getGoalsByKraId(Long kraId);
    List<GoalDTO> getGoalsByEmployeeId(Long employeeId);

}
