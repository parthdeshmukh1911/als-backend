package com.als.employee;

import java.io.ByteArrayInputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.als.converters.ExcelHelper;
import com.als.dto.EmployeeDTO;

@RestController
@RequestMapping("/employees")
@CrossOrigin
public class EmployeeController {
	private final EmployeeService employeeService;
	
	
	private EmployeePDFExporter employeePDFExporter;

	@Autowired
	public EmployeeController(EmployeeService employeeService,EmployeePDFExporter employeePDFExporter) {
		this.employeeService = employeeService;
		
		this.employeePDFExporter= employeePDFExporter;
	}

	@GetMapping("/byname/{name}")
	public ResponseEntity<Employee> getEmployeeByName(@PathVariable String name) {
		Employee employee = employeeService.getEmployeeByName(name);
		if (employee != null) {
			return ResponseEntity.ok(employee);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/byemail/{email}")
	public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
		Employee employee = employeeService.getEmployeeByEmailId(email);
		if (employee != null) {
			return ResponseEntity.ok(employee);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/manager/{managerId}")
	public ResponseEntity<List<Employee>> getEmployeesByManagerId(@PathVariable Long managerId) {
		try {
			List<Employee> employees = employeeService.getEmployeesByManagerId(managerId);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (Exception e) {
			// Handle the exception or rethrow it
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<Employee>> getEmployeesByStatus(@PathVariable String status) {
		try {
			List<Employee> employees = employeeService.getEmployeesByStatus(status);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (Exception e) {
			// Handle the exception or rethrow it
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{employeeId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
		try {
			Employee employee = employeeService.getEmployeeById(employeeId);
			if (employee != null) {
				return new ResponseEntity<>(employee, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Handle the exception or rethrow it
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employee = employeeService.getAllEmployees();
		if (employee != null) {
			return ResponseEntity.ok(employee);
		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@PutMapping
	  public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeDTO employee) {
	    try {
	      Employee updatedEmployee = employeeService.updateEmployee(employee);
	      return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	    } catch (Exception e) {
	      // Handle the exception or rethrow it
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	@PutMapping("/update")
    public ResponseEntity<Employee> updateEmployees(@RequestBody Employee employee) {
        try {
            // Validate if the employee exists
            Employee existingEmployee = employeeService.getEmployeeById(employee.getId());
            if (existingEmployee == null) {
                throw new EmployeeException("Employee not found");
            }
    
            // Call the service to update the employee
            Employee updatedEmployee = employeeService.updateEmployees(employee);
            
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            // Handle any specific exceptions or perform any necessary logging or error handling
            throw new EmployeeException("Failed to update employee", e);
        }
    }
	

	@PostMapping
	public ResponseEntity<Employee> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
		try {
			Employee savedEmployee = employeeService.saveEmployee(employeeDTO);
			return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
		} catch (Exception e) {
			// Handle the exception or rethrow it
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<String> updateEmployeeStatus(@PathVariable("id") Long id) {
		String message;
		try{
			employeeService.updateEmployeeStatusById(id);
			return ResponseEntity.ok("Employee status updated successfully.");
		}catch(Exception e){
			message = "Could not update Status of employee!!!!";
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<String> saveEmployeeFromExcel(@RequestParam("file") MultipartFile file) {
		String message;
		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				employeeService.saveExcelFile(file);

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
	public ResponseEntity<Resource> getFile() {
		String filename = "employee.xlsx";
		InputStreamResource file = new InputStreamResource(employeeService.getExcelFileForAllTheEmployees());

		return ResponseEntity.ok().header("attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(file);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> getFile(Long id) {
		String filename = "employee.xlsx";
		InputStreamResource file = new InputStreamResource(employeeService.getExcelFileForEmployeesUnderManager(id));

		return ResponseEntity.ok().header("attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@DeleteMapping("/{employeeId}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
		try {
			employeeService.deleteEmployee(employeeId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// Handle the exception or rethrow it
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	


    @GetMapping("/manager/{managerId}/pdf")
    public ResponseEntity<InputStreamResource> exportEmployeesToPDF(
        @PathVariable Long managerId) {
        try {

            List<Employee> employees = employeeService.getEmployeesByManagerId(managerId);

            ByteArrayInputStream inputStream = employeePDFExporter.exportToPDF(employees);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_list.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            // Handle the exception or rethrow it
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
