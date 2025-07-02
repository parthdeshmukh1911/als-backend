package com.als.goal;

import com.als.kra.KRA;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "goal")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String goalName;

    @Column(columnDefinition = "TEXT")
    private String goalDescription;

    @Column(nullable = false)
    private String expectedValue;

    @Column(nullable = false)
    private String aPlusPlus;

    @Column(nullable = false)
    private String aPlus;

    @Column(nullable = false)
    private String a;

    @Column(nullable = false)
    private String b;

    @Column(nullable = false)
    private String c;

    private String actualValue;

    @Column(nullable = false)
    private int percentage; // This must sum up to 100 across all goals of a KRA

    @ManyToOne
    @JoinColumn(name = "kra_id", nullable = false)
    private KRA kra;
}
