package com.als.dto;



import java.time.LocalDate;



import lombok.Data;

@Data
public class RosterDTO {
    private Long rosterId;
    private Long employeeId;
    private LocalDate rosterDate;
    private String rosterType;
    
    // Constructors, getters, and setters
}
