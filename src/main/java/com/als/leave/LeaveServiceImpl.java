package com.als.leave;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.als.attendance.Attendance;
import com.als.attendance.AttendanceRepository;
import com.als.dto.LeaveDTO;
import com.als.emailservice.EmailService;
import com.als.employee.Employee;
import com.als.employee.EmployeeService;
import com.als.leavebalance.LeaveBalance;
import com.als.leavebalance.LeaveBalanceService;
import com.als.leavecategory.LeaveCategory;
import com.als.leavecategory.LeaveCategoryRepository;
import com.als.roster.Roster;
import com.als.roster.RosterService;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmailService emailService;
    private final EmployeeService employeeService;
    private final LeaveBalanceService leaveBalanceService;
    private final ModelMapper modelMapper;
    private final LeaveCategoryRepository leaveCategoryRepository;
    private final RosterService rosterService;
    private final AttendanceRepository attendanceService;
    @Autowired
    public LeaveServiceImpl(LeaveRepository leaveRepository, EmailService emailService,
                            EmployeeService employeeService, LeaveBalanceService leaveBalanceService,
                            ModelMapper modelMapper, LeaveCategoryRepository leaveCategoryRepository,
                            AttendanceRepository attendanceService,RosterService rosterService) {
        this.leaveRepository = leaveRepository;
        this.emailService = emailService;
        this.employeeService = employeeService;
        this.leaveBalanceService = leaveBalanceService;
        this.modelMapper = modelMapper;
        this.leaveCategoryRepository=leaveCategoryRepository;
        this.attendanceService = attendanceService;
        this.rosterService = rosterService;
    }

    @Override
    public List<Leave> getEmployeeLeaves(Long employeeId, LocalDate startDate, LocalDate endDate) {
        try {
            List<Leave> leaves = leaveRepository.findByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);
            return leaves;
        } catch (Exception e) {
            throw new LeaveException("Failed to retrieve employee leaves by ID and date range", e);
        }
    }

    @Override
    public List<Leave> getLeavesByStatus(String status) {
        try {
            List<Leave> leaves = leaveRepository.findByStatus(status);
            return leaves;
        } catch (Exception e) {
            throw new LeaveException("Failed to retrieve leaves by status", e);
        }
    }

    @Override
    public Leave getLeaveById(Long leaveId) {
        try {
            Leave leave = leaveRepository.findById(leaveId).orElse(null);
            return leave;
        } catch (Exception e) {
            throw new LeaveException("Failed to retrieve leave by ID", e);
        }
    }

    @Override
    public void deleteLeave(Long leaveId) {
        try {
            leaveRepository.deleteById(leaveId);
        } catch (Exception e) {
            throw new LeaveException("Failed to delete leave", e);
        }
    }

    @Override
    public List<Leave> getAllLeavesByEmployeeId(Long employeeId) {
        try {
            List<Leave> leaves = leaveRepository.findByEmployeeId(employeeId);
            return leaves;
        } catch (Exception e) {
            throw new LeaveException("Failed to retrieve leaves by employee ID", e);
        }
    }

    @Override
    public Leave applyLeave(LeaveDTO leaveDTO) {
        try {
            System.out.println("Mapping DTO to Entity...");
            Leave leave = modelMapper.map(leaveDTO, Leave.class);

            System.out.println("2Fetching employee...");
            Employee employee = employeeService.getEmployeeById(leaveDTO.getEmployeeId());
            leave.setEmployee(employee);

            System.out.println("3Fetching leave category...");
            LeaveCategory leaveCategory = leaveCategoryRepository.findById(leaveDTO.getLeaveCategory()).orElse(null);
            leave.setLeaveCategory(leaveCategory);

            System.out.println("4Saving leave...");
            Leave appliedLeave = leaveRepository.save(leave);

			/*
			 * System.out.println("5Checking manager..."); if (employee.getManager() !=
			 * null) { System.out.println("6️⃣ Fetching manager..."); Employee manager =
			 * employeeService.getEmployeeById(employee.getManager().getId());
			 * 
			 * String to = manager.getEmailId(); String subject = "Leave Application From: "
			 * + employee.getFirstName() + " " + employee.getLastName(); String body =
			 * "Dear " + manager.getFirstName() + ",\n\n" +
			 * "An employee has applied for leave.\n" + "Employee: " +
			 * employee.getFirstName() + " " + employee.getLastName() + "\n" +
			 * "Leave Dates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\n\n"
			 * + "Reason: " + leave.getReason() + "\n\n" +
			 * "Please take necessary action.\n\n" + "Regards,\n" +
			 * "Leave Management System";
			 * 
			 * System.out.println("7️⃣ Sending email to: " + to); emailService.sendMail(to,
			 * subject, body); } else {
			 * System.out.println("⚠️ No manager assigned for employee ID: " +
			 * employee.getId()); }
			 * 
			 * System.out.println("✅ Leave applied successfully.");
			 */
            return appliedLeave;

        } catch (Exception e) {
            System.err.println("❌ Exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new LeaveException("Failed to save leave", e);
        }
    }



    @Override
    public List<Leave> getLeaveByManagerId(Long managerId) {
    try{
        List<Employee> employeeUnderManager = employeeService.getEmployeesByManagerId(managerId);
        List<Leave> leavesOfEmployeesUnderManager = new ArrayList<>();
        for(Employee employee : employeeUnderManager){
            List<Leave> leavesOfEmployee=leaveRepository.findByEmployeeIdAndStatus(employee.getId(), "Pending");
            if(leavesOfEmployee.size() != 0){
                leavesOfEmployeesUnderManager.addAll(leavesOfEmployee);
            }
        }
        return leavesOfEmployeesUnderManager;
    }catch(Exception e){
        throw new LeaveException("Failed to fetch leave", e);
    }
    
    


    }
    private int getWeekdaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        int weekdays = 0;
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                weekdays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return weekdays;
    }
    @Override
    public void updateLeave(Long leaveId , String status) {
        try {
            leaveRepository.updateLeaveStatusById(leaveId , status);
            Leave leave = leaveRepository.findById(leaveId).get();
            Employee employee = employeeService.getEmployeeById(leave.getEmployee().getId());
            Employee manager = employeeService.getEmployeeById(employee.getManager().getId());

            if(status.equalsIgnoreCase("Approved")) {

                // Retrieve the corresponding leave balance
                LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceByEmployeeAndCategory(
                        leave.getEmployee().getId(),
                        leave.getLeaveCategory().getLeaveCategoryId()
                );

                if (leaveBalance != null) {
                    int leaveDuration = getWeekdaysBetweenDates(leave.getStartDate(), leave.getEndDate());
                    int updatedBalance = leaveBalance.getBalance() - leaveDuration;

                    if (updatedBalance >= 0) {
                        // Update the leave balance
                        leaveBalance.setBalance(updatedBalance);
                        leaveBalanceService.updateLeaveBalance(
                                leave.getEmployee().getId(),
                                leave.getLeaveCategory().getLeaveCategoryId(),
                                updatedBalance
                        );
                        System.out.println("Leave balance updated");
                    } else {
                        throw new LeaveException("Insufficient leave balance");
                    }
                } else {
                    throw new LeaveException("Leave balance not found");
                }

                System.out.println("Leave : " + leave);

                // Update attendance table
                LocalDate today = LocalDate.now();
                LocalDate startDate = leave.getStartDate();
                LocalDate endDate = leave.getEndDate();
                LocalDate currentDate = startDate;

                List<Attendance> alreadyMarkedAsPresent = attendanceService.findByEmployeeIdAndAttendanceDateBetween(
                        employee.getId(), startDate, endDate
                );
                System.out.println("AlreadyMarkedAsPresent : " + alreadyMarkedAsPresent);
                
                if (!alreadyMarkedAsPresent.isEmpty()) {
                    System.out.println("Inside Already marked attendance");
                    for (Attendance attendance : alreadyMarkedAsPresent) {
                        attendance.setAttendanceStatus("On Leave");
                    }
                    attendanceService.saveAll(alreadyMarkedAsPresent);
                }

                if (alreadyMarkedAsPresent.isEmpty()) {
                    System.out.println("CurrentDate : " + currentDate + " EndDate: " + endDate);
                    while (!currentDate.isAfter(endDate)) {
                        System.out.println("Adding attendance");
                        Attendance attendance = new Attendance();
                        attendance.setEmployee(employee);
                        attendance.setAttendanceDate(currentDate);
                        attendance.setAttendanceTime(LocalTime.now());
                        attendance.setAttendanceStatus("On Leave");

                        Roster roster = null;
                        try {
                            roster = rosterService.findByEmployeeIdAndRosterDate(employee.getId(), currentDate);
                        } catch (Exception e) {
                            new Exception("Roster is not found!!!!");
                        }

                        if (roster != null) {
                            attendance.setWorkMode(roster.getRosterType());
                        } else {
                            attendance.setWorkMode("Roster not updated");
                        }

                        System.out.println("Saving attendance : " + attendanceService.save(attendance));
                        currentDate = currentDate.plusDays(1);
                    }
                }
            }

            // Commented: Mail sending to employee
            /*
            if (employee != null) {
                String to = employee.getEmailId();
                String subject = "Leave Application From: " + employee.getFirstName() + " " + employee.getLastName();
                String body = "Dear " + employee.getFirstName() + "\n\n" +
                        "Your leave has been " + leave.getStatus() + " by " + manager.getFirstName() + "\n" +
                        "Leave Dates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\n\n" +
                        "Reason: " + leave.getReason() + "\n\n" +
                        "Status: " + leave.getStatus() + "\n\n" +
                        "Regards,\n" +
                        "Leave Management System";

                emailService.sendMail(to, subject, body);
            }
            */

        } catch (Exception e) {
            throw new LeaveException("Failed to update leave", e);
        }
    }



}
