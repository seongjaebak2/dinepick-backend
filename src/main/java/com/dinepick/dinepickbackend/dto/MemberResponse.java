package com.dinepick.dinepickbackend.dto;

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

    /**
     * Member 엔티티 객체를 MemberResponse DTO로 변환하는 정적 팩토리 메서드
     * - 엔티티 내부 구조가 변경되어도 API 스펙이 유지되도록 보호함
     * @param member 변환할 회원 엔티티
     * @return 변환된 DTO 객체
     */
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getRole().name() // Enum 형태의 권한을 문자열로 변환
        );
    }
}

