package com.als.dto;

import lombok.Data;

@Data
public class GoalDTO {
    private Long id;
    private Long kraId;

    private String goalName;
    private String goalDescription;

    private String expectedValue;

    private String aPlusPlus;
    private String aPlus;
    private String a;
    private String b;
    private String c;

    private String actualValue;

    private int percentage;
}

