package com.dinepick.dinepickbackend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE("정상"),
    SUSPENDED("정지"),
    WITHDRAWN("탈퇴");

    private final String description;
}