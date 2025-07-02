package com.als.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class KRADTO {
    private Long id; // Optional during create
    private Long employeeId;
    private String financialYear;
    private String status;           // Optional during create
    private LocalDate createdDate;   // Optional during create
    private String firstName;
    private String lastName;
    private String kraName;
    private String kraDescription;

}
