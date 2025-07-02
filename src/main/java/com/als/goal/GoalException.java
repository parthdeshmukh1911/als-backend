package com.als.goal;

import java.io.Serializable;

public class GoalException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public GoalException(String message) {
        super(message);
    }

    public GoalException(String message, Throwable cause) {
        super(message, cause);
    }
}
