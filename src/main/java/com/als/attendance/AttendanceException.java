package com.als.attendance;

import java.io.Serializable;

public class AttendanceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public AttendanceException(String message) {
        super(message);
    }

    public AttendanceException(String message, Throwable cause) {
        super(message, cause);
    }
}


