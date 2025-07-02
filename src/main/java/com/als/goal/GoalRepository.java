package com.als.goal;

import com.als.goal.Goal;
import com.als.kra.KRA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByKra(KRA kra);

    List<Goal> findByKraId(Long kraId);
    List<Goal> findByKraEmployeeId(Long employeeId);


    void deleteByKraId(Long kraId);
    
}
