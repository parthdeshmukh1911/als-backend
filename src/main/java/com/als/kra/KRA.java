package com.als.kra;

import com.als.employee.Employee;
import com.als.goal.Goal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "kra")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class KRA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kraName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String kraDescription;

    @Column(nullable = false)
    private String financialYear;

    @Column(nullable = false)
    private String status = "Pending"; // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "kra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;

    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now();
}
