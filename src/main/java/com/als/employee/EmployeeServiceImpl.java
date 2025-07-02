package com.als.employee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.als.auth.AuthenticationService;
import com.als.auth.RegisterRequest;
import com.als.converters.ExcelHelper;
import com.als.dto.EmployeeDTO;
import com.als.emailservice.EmailService;
import com.als.leavebalance.LeaveBalance;
import com.als.leavebalance.LeaveBalanceRepository;
import com.als.leavecategory.LeaveCategory;
import com.als.leavecategory.LeaveCategoryService;
import com.als.user.Role;


@Service
public class EmployeeServiceImpl implements EmployeeService {
	
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private LeaveCategoryService leaveCategoryService;
    @Autowired
    private LeaveBalanceRepository leaveBalanceService;
    @Autowired
    private EmailService emailService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // ...

    @Override
    public Employee getEmployeeByName(String name) {
        List<Employee> employees = employeeRepository.findByFirstName(name);
        if (employees.isEmpty()) {
            throw new EmployeeException("Employee not found for name: " + name);
        }
        return employees.get(0);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new EmployeeException("Employee data is empty");
        }
        return employees;
    }

    @Override
    public List<Employee> getEmployeesByManagerId(Long managerId) {
        try {
        	return employeeRepository.findByManagerId(managerId);
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve employees by manager ID", e);
        }
    }

    @Override
    public List<Employee> getEmployeesByStatus(String status) {
        try {
        	return employeeRepository.findByStatus(status);
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve employees by status", e);
        }
    }

    @Override
    public Employee getEmployeeById(Long employeeId) {
        try {
        	System.out.println("Employee id is : " + employeeId);
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeException("Employee not found for ID: " + employeeId));
//            System.out.println("Employee : "+ employee);
            return employee;
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve employee by ID " + employeeId ,   e);
        }
    }

    @Override
    public Employee getEmployeeByEmailId(String email) {
        return employeeRepository.findByEmailId(email);
    }
    
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(EmployeeDTO employeeDTO) {
        try {
           Employee employee = modelMapper.map(employeeDTO, Employee.class);
             if (employeeDTO.getManagerId() != null) {
                 Employee manager = employeeRepository.findById(employeeDTO.getManagerId())
                         .orElseThrow(() -> new EmployeeException("Manager not found for ID: " + employeeDTO.getManagerId()));
                 employee.setManager(manager);
                

             }
             Employee savedEmployee = employeeRepository.save(employee);
          return savedEmployee;   
        } catch (Exception e) {
            throw new EmployeeException("Failed to update employee", e);
        }
    }

    @Override
    public void updateEmployeeStatusById(Long employeeId) {
        try {
        	String status = "Inactive";
          employeeRepository.updateEmployeeStatusById(employeeId,status);
          Employee employee=employeeRepository.findById(employeeId).get();
          authenticationService.deleteUser(employee.getEmailId());
        } catch (Exception e) {
            throw new EmployeeException("Failed to update employee", e);
        }
    }
    
    @Override
    public Employee updateEmployees(Employee employee) {
        try {
            Employee existingEmployee = employeeRepository.findById(employee.getId())
                    .orElseThrow(() -> new EmployeeException("Employee not found"));
            existingEmployee.setContactNumber(employee.getContactNumber());
            existingEmployee.setEmergencyContactNumber(employee.getEmergencyContactNumber());
            existingEmployee.setMaritalStatus(employee.getMaritalStatus());
            existingEmployee.setDateOfJoining(employee.getDateOfJoining());
            existingEmployee.setDesignation(employee.getDesignation());
            existingEmployee.setJobTitle(employee.getJobTitle());
            existingEmployee.setEmployeeType(employee.getEmployeeType());
            
            return employeeRepository.save(existingEmployee);
        } catch (Exception e) {
            throw new EmployeeException("Failed to update employee", e);
        }
    }


    private void addEmployeeLeaveCategory(Employee employee) {
        Employee employee1 = employeeRepository.findByEmailId(employee.getEmailId());
        List<LeaveCategory> leaveCategoryList = leaveCategoryService.getAllLeaveCategories();
        List<LeaveBalance> leaveBalanceList = new ArrayList<>();
        for (LeaveCategory leaveCategory : leaveCategoryList) {
            LeaveBalance leaveBalance = new LeaveBalance();

            if (employee1.getGender().equalsIgnoreCase("male")) {
                if (leaveCategory.getLeaveCategoryName().equalsIgnoreCase("Paternity Leave")) {
                    leaveBalance.setLeaveCategory(leaveCategory);
                    leaveBalance.setEmployee(employee1);
                    leaveBalance.setBalance(10);
                    leaveBalanceList.add(leaveBalance);
                } else {
                    if(!leaveCategory.getLeaveCategoryName().equalsIgnoreCase("Maternity Leave")){
                        leaveBalance.setLeaveCategory(leaveCategory);
                        leaveBalance.setEmployee(employee1);
                        leaveBalance.setBalance(15);
                        leaveBalanceList.add(leaveBalance);
                    }
                }
            } else if (employee1.getGender().equalsIgnoreCase("female")) {
                if (leaveCategory.getLeaveCategoryName().equalsIgnoreCase("Maternity Leave")) {
                    leaveBalance.setLeaveCategory(leaveCategory);
                    leaveBalance.setEmployee(employee1);
                    leaveBalance.setBalance(180);
                    leaveBalanceList.add(leaveBalance);
                } else {
                    if(!leaveCategory.getLeaveCategoryName().equalsIgnoreCase("Paternity Leave")){
                        leaveBalance.setLeaveCategory(leaveCategory);
                        leaveBalance.setEmployee(employee1);
                        leaveBalance.setBalance(15);
                        leaveBalanceList.add(leaveBalance);
                    }
                }
            } else {
                leaveBalance.setLeaveCategory(leaveCategory);
                leaveBalance.setEmployee(employee1);
                leaveBalance.setBalance(15);
                leaveBalanceList.add(leaveBalance);
            }
        }
        leaveBalanceService.saveAll(leaveBalanceList);
    }


    @Override
    public Employee saveEmployee(EmployeeDTO employeeDTO) {
        try {
            System.out.println(">> [DEBUG] Received EmployeeDTO: " + employeeDTO);

            Employee employee = modelMapper.map(employeeDTO, Employee.class);
            System.out.println(">> [DEBUG] Mapped Employee: " + employee.getEmailId());

            if (employeeDTO.getManagerId() != null) {
                Employee manager = employeeRepository.findById(employeeDTO.getManagerId())
                        .orElseThrow(() -> new EmployeeException("Manager not found for ID: " + employeeDTO.getManagerId()));
                employee.setManager(manager);
                System.out.println(">> [DEBUG] Manager assigned: " + manager.getEmailId());

                if (manager.getDepartment().getDepartmentId() != 1) {
                    authenticationService.updateRole(manager.getEmailId(), Role.MANAGER);
                } else {
                    authenticationService.updateRole(manager.getEmailId(), Role.ADMIN);
                }
            }

            Employee savedEmployee = employeeRepository.save(employee);
            System.out.println(">> [DEBUG] Employee saved with ID: " + savedEmployee.getId());

            // Adding user
            RegisterRequest user = new RegisterRequest();
            user.setUsername(employee.getFirstName());
            user.setEmail(employee.getEmailId());
            String rawPassword = employee.getFirstName() + "@123";
            user.setPassword(rawPassword);
            Long deptId = employeeDTO.getDepartmentId();

            if (deptId == 1) {
                user.setRole(Role.HR);
            } else {
                user.setRole(Role.USER);
            }

            System.out.println(">> [DEBUG] Registering user with email: " + user.getEmail() + ", password: " + rawPassword);
            authenticationService.register(user);

            // Adding leaves for the employee
            addEmployeeLeaveCategory(savedEmployee);
            System.out.println(">> [DEBUG] Leave category added");

            // Sending welcome email
            if (employee != null) {
                String to = employee.getEmailId();
                String subject = "Welcome to the Team " + employee.getFirstName() + " " + employee.getLastName();
                String body = "Dear " + employee.getFirstName() + ",\n\n" +
                        "This email contains your login credentials.\n" +
                        "UserId: " + employee.getEmailId() + "\n" +
                        "Password: " + rawPassword + "\n\n" +
                        "Regards,\n" +
                        "Leave Management System";

                try {
                    System.out.println(">> [DEBUG] Sending email to: " + to);
                    emailService.sendMail(to, subject, body);
                    System.out.println(">> [DEBUG] Email sent successfully");
                } catch (Exception e) {
                    System.out.println(">> [ERROR] Failed to send email: " + e.getMessage());
                }
            }

            return savedEmployee;

        } catch (Exception ex) {
            System.out.println(">> [ERROR] Exception while saving employee: " + ex.getMessage());
            ex.printStackTrace();
            throw new EmployeeException("Failed to save employee", ex);
        }
    }


    @Override
    public void deleteEmployee(Long employeeId) {
        try {
            employeeRepository.deleteById(employeeId);
        } catch (Exception e) {
            throw new EmployeeException("Failed to delete employee", e);
        }
    }

    @Override
    public List<Employee> getHybridEmployees() {
        try {
        	return employeeRepository.findByEmployeeType("Hybrid");
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve hybrid employees", e);
        }
    }

    @Override
    public List<Employee> getVirtualEmployees() {
        try {
            return employeeRepository.findByEmployeeType("Virtual");
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve virtual employees", e);
        }
    }

	@Override
	public void saveExcelFile(MultipartFile file) {
		List<EmployeeDTO> list;
		List<Employee> employees = new ArrayList<Employee>();
		try {
			//list contains list of employeeDto from Excel file
			list = ExcelHelper.excelToEmployeeConvertor(file.getInputStream());
			
			for(EmployeeDTO employeeDTO : list) {
				Employee employee = modelMapper.map(employeeDTO, Employee.class);
				if(employeeDTO.getManagerId()==null) {
					System.out.println("From EmployeeDTO manager is null : " + employeeDTO);
				}
				Employee manager = employeeRepository.findById(employeeDTO.getManagerId())
						.orElseThrow(() -> new EmployeeException("Manager not found for ID: " + employeeDTO.getManagerId()));
				
				employee.setManager(manager);
				 if(manager.getDepartment().getDepartmentId()!=1){
		                authenticationService.updateRole(manager.getEmailId() , Role.MANAGER);
		            }else{
		                authenticationService.updateRole(manager.getEmailId() , Role.ADMIN);
		            }
                //Adding user
                RegisterRequest user = new RegisterRequest();
                user.setUsername(employee.getFirstName());
                user.setEmail(employee.getEmailId());
                user.setPassword(employee.getFirstName()+"@123");
                Long deptId = employeeDTO.getDepartmentId();
                if(deptId == 1) {
                	user.setRole(Role.HR);;
                }else {
                	user.setRole(Role.USER);
                }
                authenticationService.register(user);

                //Sending email to the user
                if (employee != null ) {
                    String to = employee.getEmailId();
                    String subject = "Welcome to the Team  " + employee.getFirstName() + " " + employee.getLastName();
                    String body = "Dear " + employee.getFirstName() + ",\n\n" +
                            "This email contains your login credentials.\n" +
                            "UserId: " + employee.getEmailId() + "\n" +
                            "Password " + employee.getFirstName()+"@123" + "\n\n" +
                            "Regards,\n" +
                            "Leave Management System";

                    emailService.sendMail(to, subject, body);
                }

				employees.add(employee);
			}
			employeeRepository.saveAll(employees);
            for(Employee employee : employees) {
                //adding leaves for the employee
                addEmployeeLeaveCategory(employee);
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
		
	}

	@Override
	public ByteArrayInputStream getExcelFileForAllTheEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		
		ByteArrayInputStream in = ExcelHelper.employeeToExcelConvertor(employees);
		
		return in;
	}
	@Override
	public ByteArrayInputStream getExcelFileForEmployeesUnderManager(Long id) {
		List<Employee> employees = employeeRepository.findByManagerId(id);
		
		ByteArrayInputStream in = ExcelHelper.employeeToExcelConvertor(employees);
		
		return in;
		
	}

	@Override
    public List<Employee> getEmployeesByManagerIdAndDateOfJoiningRange(Long managerId, LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByManagerIdAndDateOfJoiningBetween(managerId, startDate, endDate);
    }
}
