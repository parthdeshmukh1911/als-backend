package com.als.publicholiday;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "public_holiday")
@Data
public class PublicHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publicHolidayId;
    
    private LocalDate holidayDate;
    
    private String holidayName;
}
