package com.dinepick.dinepickbackend.exception.member;

import com.dinepick.dinepickbackend.exception.BaseException;
import org.springframework.http.HttpStatus;

public abstract class MemberException extends BaseException {

    protected MemberException(
            HttpStatus status,
            String errorCode,
            String message
    ) {
        super(status, errorCode, message);
    }
}

