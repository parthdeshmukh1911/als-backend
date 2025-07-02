package com.als.attendance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.als.dto.AttendanceDTO;
import com.als.employee.Employee;
import com.als.employee.EmployeeService;
import com.als.employee.EmployeeServiceImpl;

@RestController
@RequestMapping("/attendances")
@CrossOrigin
public class AttendanceController {
    private final AttendanceService attendanceService;
    
    private final EmployeeService employeeService;

    private final AttendancePDFExporter attendancePDFExporter;

    @Autowired
    public AttendanceController(AttendanceService attendanceService,EmployeeServiceImpl employeeService,AttendancePDFExporter attendancePDFExporter) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
        this.attendancePDFExporter = attendancePDFExporter;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Attendance>> getEmployeeAttendance(
            @PathVariable Long employeeId,
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate
    ) {
        try {
            List<Attendance> attendance = attendanceService.getEmployeeAttendance(employeeId, startDate, endDate);
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Attendance>> getAttendanceByStatus(@PathVariable String status) {
        try {
            List<Attendance> attendance = attendanceService.getAttendanceByStatus(status);
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todayAttendance/{employeeId}")
    public ResponseEntity<Attendance> getTodayAttendance(@PathVariable Long employeeId) {
        try {
            Attendance attendance = attendanceService.getEmployeeAttendanceForToday(employeeId);
            if (attendance == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 → handled gracefully
            }
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping
    public ResponseEntity<Void> updateAttendance(
            @RequestParam(name = "attendanceId") Long attendanceId,
            @RequestParam(name = "status") String status
    ) {
        try {
           attendanceService.updateAttendance(attendanceId, status);
           return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        try {
            Attendance savedAttendance = attendanceService.saveAttendance(attendanceDTO);
            return new ResponseEntity<>(savedAttendance, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        try {
            attendanceService.deleteAttendance(attendanceId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Scheduled(cron = "0 0 11 * * MON-FRI")
    public ResponseEntity<Void> sendAttendanceReminder() {
        try {
            attendanceService.sendAttendanceReminders();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Scheduled(cron = "0 07 22 * * MON-SAT")
    @GetMapping("/runScheduler")
    public ResponseEntity<Void> saveEmployeeAttendance() {
        try {
            attendanceService.saveEmployeeAttendance();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    
    @GetMapping("/{employeeId}/export")
    public ResponseEntity<InputStreamResource> exportAttendanceToPDF(
            @PathVariable Long employeeId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        List<Attendance> attendanceList = attendanceService.getEmployeeAttendance(employeeId, startDate, endDate);
        ByteArrayInputStream pdfData = attendancePDFExporter.exportToPDF(attendanceList);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfData));
    }
    
    
    
    @GetMapping("/{managerId}/employees/export")
    public ResponseEntity<InputStreamResource> exportAttendancesToPDF(
            @PathVariable Long managerId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) throws IOException {
    	List<Employee> employeeList = employeeService.getEmployeesByManagerId(managerId);
    	List<Attendance> attendanceList = new ArrayList<Attendance>();
    	for (Employee employee:employeeList) {
    		Long employeeId = employee.getId();
    		List<Attendance> attendanceofEmployee = attendanceService.getEmployeeAttendance(employeeId, startDate, endDate);    		
    		for (Attendance attendance : attendanceofEmployee) {    			
    			attendanceList.add(attendance);
    		}
    	}    		       
		ByteArrayInputStream pdfData = attendancePDFExporter.exportToPDF(attendanceList);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance.pdf");
		return ResponseEntity
		        .ok()
		        .headers(headers)
		        .contentType(MediaType.APPLICATION_PDF)
		        .body(new InputStreamResource(pdfData));
    }
    
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportAllAttendanceToPDF(
            
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        List<Attendance> attendanceList = attendanceService.findAllAttendanceDateBetween(startDate, endDate);
        ByteArrayInputStream pdfData = attendancePDFExporter.exportToPDF(attendanceList);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfData));
    }

}
