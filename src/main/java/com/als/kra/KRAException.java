package com.als.kra;

import java.io.Serializable;

public class KRAException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public KRAException(String message) {
        super(message);
    }

    public KRAException(String message, Throwable cause) {
        super(message, cause);
    }
}
