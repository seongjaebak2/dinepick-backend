package com.dinepick.dinepickbackend.exception.auth;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends AuthException {

    public AccessDeniedException() {
        super(
                HttpStatus.FORBIDDEN,
                "AUTH_ACCESS_DENIED",
                "접근 권한이 없습니다."
        );
    }
}

