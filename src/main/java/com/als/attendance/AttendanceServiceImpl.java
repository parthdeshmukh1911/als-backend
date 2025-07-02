package com.als.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.als.dto.AttendanceDTO;
import com.als.emailservice.EmailService;
import com.als.employee.Employee;
import com.als.employee.EmployeeService;
import com.als.publicholiday.PublicHoliday;
import com.als.publicholiday.PublicHolidayService;
import com.als.roster.Roster;
import com.als.roster.RosterService;

@Service

public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;

    private final EmployeeService employeeRepository;

    private final EmailService emailService;

    private final ModelMapper modelMapper;

    private final RosterService rosterService;
    
    private final PublicHolidayService holidayService;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeService employeeRepository, EmailService emailService, ModelMapper modelMapper, RosterService rosterService, PublicHolidayService holidayService) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.rosterService=rosterService;
        this.holidayService=holidayService;
    }

    @Override
    public List<Attendance> getEmployeeAttendance(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(employeeId, startDate, endDate);
        return attendances;
    }

    @Override
    public List<Attendance> getAttendanceByStatus(String status) {
        List<Attendance> attendances = attendanceRepository.findByAttendanceStatus(status);
       
        return attendances;
    }

    @Override
    public Attendance saveAttendance(AttendanceDTO attendanceDTO) {
        Attendance attendance = modelMapper.map(attendanceDTO, Attendance.class);
        attendance = attendanceRepository.save(attendance);
        return attendance;
    }

    @Override
    public void updateAttendance(Long attendanceId, String status) {
    	try {
    		System.out.println("Data : " + attendanceId + status);
    		attendanceRepository.updateAttendanceStatusById(attendanceId, status);
    		
    	}catch(Exception e) {
    		System.out.println("Failed to Update status of attendance");
    	}
        
    }
    
    

    @Override
    public Attendance getEmployeeAttendanceForToday(Long employeeId) {
        LocalDate currentDate = LocalDate.now();
        //return attendanceRepository.findByEmployeeIdAndAttendanceDate(employeeId, currentDate).get();
        return attendanceRepository.findByEmployeeIdAndAttendanceDate(employeeId, currentDate)
                .orElse(null);
    }

    @Override
    public void deleteAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }
    
    @Override
    public List<Attendance> findAllAttendanceDateBetween(LocalDate startDate, LocalDate endDate) {
       return attendanceRepository.findByAttendanceDateBetween(startDate, endDate);
    }

    @Override
    public void sendAttendanceReminders() {
        LocalDate currentDate = LocalDate.now();
        LocalDate yesterday = currentDate.minusDays(1);
        List<Attendance> missingAttendances = attendanceRepository.findByAttendanceDate(yesterday);

        if (!missingAttendances.isEmpty()) {
            for (Attendance attendance : missingAttendances) {
                if(attendance.getAttendanceStatus().equals("Absent")){
                    Employee employee = employeeRepository.getEmployeeById(attendance.getEmployee().getId());
                    String to = employee.getEmailId();
                    String subject = "Attendance Remainder";
                    String body = "Dear "+employee.getFirstName()+",\n"
                            +"you have not recorded your attendance for yesterday i.e."+yesterday+". Please do so as soon as possible.";
                    emailService.sendMail(to,subject,body);
                }
            }
        }
    }

    @Override
    public void saveEmployeeAttendance() {
        // Extract the time and date separately
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        PublicHoliday holiday = holidayService.getByDate(currentDate);
        System.out.println("Holiday present : "+holiday);
        if(holiday==null) {
        	List<Employee> employeeList = employeeRepository.findAll();
        	if (!employeeList.isEmpty()) {
                for (Employee employee : employeeList) {
                	Attendance hasMarkedAttendance= null;
                    try {
                    	hasMarkedAttendance=attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), currentDate).get();
                    }catch(Exception e) {
                    	System.out.println(" No attendance marked ");
                    }
                    if(hasMarkedAttendance==null){
                        AttendanceDTO attendance = new AttendanceDTO();
                        attendance.setEmployeeId(employee.getId());

                        //getting all the leaves
                        attendance.setAttendanceStatus("Absent");

                        attendance.setAttendanceDate(currentDate);
                        attendance.setAttendanceTime(currentTime);
                        Roster roster = null;
                        try{
                            roster = rosterService.findByEmployeeIdAndRosterDate(employee.getId());

                        }catch (Exception e){
                            new Exception("Roster is not found!!!!");
                        }
                        if(roster!= null){
                            attendance.setWorkMode(roster.getRosterType());
                        }else{
                            attendance.setWorkMode("Roster not updated");
                        }
                        AttendanceDTO attendanceDTO = modelMapper.map(attendance, AttendanceDTO.class);
                        saveAttendance(attendanceDTO);
                    }else{
                        System.out.println("Employee"+employee.getFirstName()+ "on leave ");
                    }
                }
            }
        }else {
        	System.out.println("TOday is a public holiday!!!!!!!!!!!!");
        }
    }


}