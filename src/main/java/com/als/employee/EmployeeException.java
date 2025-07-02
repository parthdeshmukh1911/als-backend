package com.als.employee;

import java.io.Serializable;

public class EmployeeException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public EmployeeException(String message) {
        super(message);
    }

    public EmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
