package com.als.roster;


import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.als.converters.ExcelHelper;
import com.als.dto.RosterDTO;

@RestController
@RequestMapping("/rosters")
@CrossOrigin
public class RosterController {
    private final RosterService rosterService;

    @Autowired
    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Roster>> getEmployeeRoster(
            @PathVariable Long employeeId,
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate
    ) {
        try {
            List<Roster> roster = rosterService.getEmployeeRosterByEmployeeId(employeeId, startDate, endDate);
            return new ResponseEntity<>(roster, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{employeeId}/{rosterType}")
    public ResponseEntity<List<Roster>> getEmployeeRosterByType(
            @PathVariable Long employeeId,
            @PathVariable String rosterType
    ) {
        try {
            List<Roster> roster = rosterService.getEmployeeRosterByType(employeeId, rosterType);
            return new ResponseEntity<>(roster, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/today/{employeeId}")
    public ResponseEntity<Roster> getTodayRosterofEmployee(
            @PathVariable Long employeeId
    ) {
        try {
            Roster roster = rosterService.findByEmployeeIdAndRosterDate(employeeId);
            return new ResponseEntity<>(roster, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Roster> updateRoster(@RequestBody RosterDTO rosterDTO) {
        try {
            Roster updatedRoster = rosterService.updateRosterByEmployeeId(rosterDTO);
            return new ResponseEntity<>(updatedRoster, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Roster> createRoster(@RequestBody RosterDTO rosterDTO) {
        try {
            Roster createdRoster = rosterService.createRosterByEmployeeId(rosterDTO);
            return new ResponseEntity<>(createdRoster, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{rosterId}")
    public ResponseEntity<Void> deleteRoster(@PathVariable Long rosterId) {
        try {
            rosterService.deleteRosterByEmployeeId(rosterId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/upload")
	public ResponseEntity<String> saveEmployeeFromExcel(@RequestParam("file") MultipartFile file) {
		String message;
		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				rosterService.saveExcelFile(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(message);
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				System.out.println(e);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
			}
		}
		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
    
    @GetMapping("/download")
	public ResponseEntity<Resource> getFile(Long id) {
		String filename = "roster.xlsx";
		InputStreamResource file = new InputStreamResource(rosterService.getExcelFileForEmployee(id));

		return ResponseEntity.ok().header("attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}
}
