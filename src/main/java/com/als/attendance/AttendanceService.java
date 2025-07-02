package com.als.attendance;

import java.time.LocalDate;
import java.util.List;

import com.als.dto.AttendanceDTO;

public interface AttendanceService {
    List<Attendance> getEmployeeAttendance(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Attendance> getAttendanceByStatus(String status);

    Attendance saveAttendance(AttendanceDTO attendance);

    void updateAttendance(Long employeeId, String status);

    void deleteAttendance(Long attendanceId);

    void sendAttendanceReminders();
    
   List<Attendance> findAllAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    void saveEmployeeAttendance();
   Attendance getEmployeeAttendanceForToday(Long employeeId);

}

