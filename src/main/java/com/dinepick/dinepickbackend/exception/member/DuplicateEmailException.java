package com.dinepick.dinepickbackend.exception.member;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends MemberException {

    public DuplicateEmailException() {
        super(
                HttpStatus.CONFLICT,
                "MEMBER_DUPLICATE_EMAIL",
                "이미 사용 중인 이메일입니다."
        );
    }
}
