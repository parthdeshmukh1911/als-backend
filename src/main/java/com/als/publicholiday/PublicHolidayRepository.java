package com.als.publicholiday;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
	
	PublicHoliday findByHolidayDate(LocalDate holidayDate);
}
