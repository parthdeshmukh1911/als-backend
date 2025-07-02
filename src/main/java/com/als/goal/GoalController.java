package com.als.goal;

import com.als.dto.GoalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@CrossOrigin
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<GoalDTO> saveGoal(@RequestBody GoalDTO goalDTO) {
        GoalDTO savedGoal = goalService.saveGoal(goalDTO);
        return ResponseEntity.ok(savedGoal);
    }

    @PutMapping
    public ResponseEntity<GoalDTO> updateGoal(@RequestBody GoalDTO goalDTO) {
        GoalDTO updatedGoal = goalService.updateGoal(goalDTO);
        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalDTO> getGoalById(@PathVariable Long goalId) {
        GoalDTO goal = goalService.getGoalById(goalId);
        return ResponseEntity.ok(goal);
    }

    @GetMapping("/kra/{kraId}")
    public ResponseEntity<List<GoalDTO>> getGoalsByKraId(@PathVariable Long kraId) {
        List<GoalDTO> goals = goalService.getGoalsByKraId(kraId);
        return ResponseEntity.ok(goals);
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<GoalDTO>> getGoalsByEmployeeId(@PathVariable Long employeeId) {
        List<GoalDTO> goals = goalService.getGoalsByEmployeeId(employeeId);
        return ResponseEntity.ok(goals);
    }

}
