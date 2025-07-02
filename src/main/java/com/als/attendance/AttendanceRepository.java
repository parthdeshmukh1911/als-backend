package com.als.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByAttendanceStatus(String status);

    List<Attendance> findByEmployeeId(Long employeeId);

    List<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    List<Attendance> findByAttendanceDate(LocalDate localDate);

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    @Modifying
    @Transactional
    @Query("UPDATE Attendance a SET a.attendanceStatus = :status WHERE a.id = :attendanceId")
    void updateAttendanceStatusById(Long attendanceId, String status);

}

