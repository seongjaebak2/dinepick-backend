package com.dinepick.dinepickbackend.exception.auth;

import com.dinepick.dinepickbackend.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseException {

    public AuthException(
            HttpStatus status,
            String errorCode,
            String message
    ) {
        super(status, errorCode, message);
    }
}
