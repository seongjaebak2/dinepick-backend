package com.dinepick.dinepickbackend.exception.member;

import org.springframework.http.HttpStatus;

public class WithdrawnMemberException extends MemberException {

    public WithdrawnMemberException() {
        super(
                HttpStatus.FORBIDDEN,
                "MEMBER_WITHDRAWN",
                "탈퇴한 회원입니다."
        );
    }
}
