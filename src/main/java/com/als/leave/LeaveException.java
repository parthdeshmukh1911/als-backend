package com.als.leave;

import java.io.Serializable;

public class LeaveException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public LeaveException(String message) {
        super(message);
    }

    public LeaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

