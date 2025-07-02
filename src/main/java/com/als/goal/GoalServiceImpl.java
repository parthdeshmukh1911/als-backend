package com.als.goal;

import com.als.dto.GoalDTO;
import com.als.kra.KRA;
import com.als.kra.KRARepository;
import com.als.kra.KRAException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private KRARepository kraRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GoalDTO saveGoal(GoalDTO goalDTO) {
        KRA kra = kraRepository.findById(goalDTO.getKraId())
                .orElseThrow(() -> new KRAException("KRA not found for ID: " + goalDTO.getKraId()));
        Goal goal = modelMapper.map(goalDTO, Goal.class);
        goal.setKra(kra);
        Goal savedGoal = goalRepository.save(goal);
        return modelMapper.map(savedGoal, GoalDTO.class);
    }

    @Override
    public GoalDTO updateGoal(GoalDTO goalDTO) {
        Goal existingGoal = goalRepository.findById(goalDTO.getId())
                .orElseThrow(() -> new GoalException("Goal not found for ID: " + goalDTO.getId()));

        modelMapper.map(goalDTO, existingGoal);
        Goal updatedGoal = goalRepository.save(existingGoal);
        return modelMapper.map(updatedGoal, GoalDTO.class);
    }

    @Override
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    @Override
    public GoalDTO getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalException("Goal not found for ID: " + goalId));
        return modelMapper.map(goal, GoalDTO.class);
    }

    @Override
    public List<GoalDTO> getGoalsByKraId(Long kraId) {
        List<Goal> goals = goalRepository.findByKraId(kraId);
        return goals.stream()
                .map(goal -> modelMapper.map(goal, GoalDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<GoalDTO> getGoalsByEmployeeId(Long employeeId) {
        return goalRepository.findByKraEmployeeId(employeeId)
                .stream()
                .map(goal -> modelMapper.map(goal, GoalDTO.class))
                .collect(Collectors.toList());
    }

}
