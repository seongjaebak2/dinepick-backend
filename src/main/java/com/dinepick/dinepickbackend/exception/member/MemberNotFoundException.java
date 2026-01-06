package com.dinepick.dinepickbackend.exception.member;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends MemberException {

    public MemberNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "MEMBER_NOT_FOUND",
                "회원이 존재하지 않습니다."
        );
    }
}
