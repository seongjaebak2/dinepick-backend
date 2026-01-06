package com.dinepick.dinepickbackend.exception.auth;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AuthException {

    public InvalidTokenException() {
        super(
                HttpStatus.UNAUTHORIZED,
                "AUTH_INVALID_TOKEN",
                "유효하지 않은 토큰입니다."
        );
    }
}
