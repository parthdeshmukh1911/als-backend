package com.als.dto;



import java.time.LocalDate;

import lombok.Data;

@Data
public class PublicHolidayDTO {
    private Long publicHolidayId;
    private LocalDate holidayDate;
    private String holidayName;

    // Constructors, getters, and setters
}
