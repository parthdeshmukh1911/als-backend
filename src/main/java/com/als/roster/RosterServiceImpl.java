package com.als.roster;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.als.converters.ExcelHelper;
import com.als.dto.RosterDTO;
import com.als.employee.Employee;
import com.als.employee.EmployeeService;

@Service
public class RosterServiceImpl implements RosterService {
    private final RosterRepository rosterRepository;
    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;

    @Autowired
    public RosterServiceImpl(RosterRepository rosterRepository, ModelMapper modelMapper, EmployeeService employeeService) {
        this.rosterRepository = rosterRepository;
        this.modelMapper = modelMapper;
        this.employeeService = employeeService;
    }

    @Override
    public List<Roster> getEmployeeRosterByEmployeeId(Long employeeId, LocalDate startDate, LocalDate endDate) {
        try {
            List<Roster> rosters = rosterRepository.findByEmployeeIdAndRosterDateBetween(employeeId, startDate, endDate);
            return rosters;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employee roster.", e);
        }
    }

    @Override
    public List<Roster> getEmployeeRosterByType(Long employeeId, String rosterType) {
        try {
            List<Roster> rosters = rosterRepository.findByEmployeeIdAndRosterType(employeeId, rosterType);
            return rosters;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employee roster by type.", e);
        }
    }

    @Override
    public List<Roster> getRosterByEmployeeId(Long employeeId) {
        try {
            List<Roster> rosters = rosterRepository.findByEmployeeId(employeeId);
            return rosters;
        } catch (Exception e) {
            throw new RuntimeException("Failed to roster by employee id.", e);
        }
    }

    public Roster findByEmployeeIdAndRosterDate(Long employeeId){
        try {
            LocalDate currentDate = LocalDate.now();
            Roster roster = rosterRepository.findByEmployeeIdAndRosterDate(employeeId,currentDate).get();
            return roster;
        } catch (Exception e) {
            throw new RuntimeException("Failed to roster by employee id.", e);
        }
    }
    
    public Roster findByEmployeeIdAndRosterDate(Long employeeId,LocalDate localDate){
        try {
            Roster roster = rosterRepository.findByEmployeeIdAndRosterDate(employeeId,localDate).get();
            return roster;
        } catch (Exception e) {
            throw new RuntimeException("Failed to roster by employee id.", e);
        }
    }

    @Override
    public Roster updateRosterByEmployeeId(RosterDTO rosterDTO) {
        try {
            Roster roster = modelMapper.map(rosterDTO, Roster.class);
            Roster updatedRoster = rosterRepository.save(roster);
            return updatedRoster;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update roster.", e);
        }
    }

    @Override
    public Roster createRosterByEmployeeId(RosterDTO rosterDTO) {
        try {
            Roster roster = modelMapper.map(rosterDTO, Roster.class);
            Roster createdRoster = rosterRepository.save(roster);
            return createdRoster;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create roster.", e);
        }
    }

    @Override
    public void deleteRosterByEmployeeId(Long rosterId) {
        try {
            rosterRepository.deleteById(rosterId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete roster.", e);
        }
    }

	@Override
	public void saveExcelFile(MultipartFile file) {
		List<RosterDTO> rosterDTOList;
		List<Roster> rosterList = new ArrayList<Roster>();
		try {
			rosterDTOList = ExcelHelper.excelToRoster(file.getInputStream());
			
			for(RosterDTO rosterDTO : rosterDTOList) {
				Roster roster = modelMapper.map(rosterDTO, Roster.class);
				Employee employee = employeeService.getEmployeeById(rosterDTO.getEmployeeId());
				roster.setEmployee(employee);
				rosterList.add(roster);
			}
			rosterRepository.saveAll(rosterList);
		}catch(IOException exception) {
			throw new RuntimeException("Failed to save Roster file" + exception.getMessage());
		}
		
	}

	@Override
	public ByteArrayInputStream getExcelFileForEmployee(Long id) {
		List<Roster> rosters = rosterRepository.findByEmployeeId(id);
	
		ByteArrayInputStream in = ExcelHelper.rosterToExcel(rosters);
		return in;
	}
}
