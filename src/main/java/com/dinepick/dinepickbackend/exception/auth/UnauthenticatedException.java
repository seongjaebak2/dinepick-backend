package com.dinepick.dinepickbackend.exception.auth;

import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends AuthException {

    public UnauthenticatedException() {
        super(
                HttpStatus.UNAUTHORIZED,
                "AUTH_UNAUTHENTICATED",
                "인증이 필요합니다."
        );
    }
}