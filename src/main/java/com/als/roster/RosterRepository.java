package com.als.roster;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RosterRepository extends JpaRepository<Roster, Long> {
    List<Roster> findByEmployeeId(Long employeeId);
    
    List<Roster> findByEmployeeIdAndRosterType(Long employeeId, String rosterType);
    
	List<Roster> findByEmployeeIdAndRosterDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    Optional<Roster> findByEmployeeIdAndRosterDate(Long employeeId, LocalDate rosterDate);
}
