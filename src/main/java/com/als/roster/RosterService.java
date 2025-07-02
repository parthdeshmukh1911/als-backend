package com.als.roster;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.als.dto.RosterDTO;

public interface RosterService {
    List<Roster> getEmployeeRosterByEmployeeId(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    List<Roster> getEmployeeRosterByType(Long employeeId, String rosterType);

    List<Roster> getRosterByEmployeeId(Long employeeId);

    Roster findByEmployeeIdAndRosterDate(Long employeeId);
    
    Roster findByEmployeeIdAndRosterDate(Long employeeId,LocalDate localDate);
    
    Roster updateRosterByEmployeeId(RosterDTO roster);
    
    Roster createRosterByEmployeeId(RosterDTO roster);
    
    void deleteRosterByEmployeeId(Long rosterId);
    
    void saveExcelFile(MultipartFile file);
    
    ByteArrayInputStream getExcelFileForEmployee(Long id);
}

