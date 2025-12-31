package com.dinepick.dinepickbackend.member.dto;

import com.dinepick.dinepickbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private String role;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getRole().name()
        );
    }
}

