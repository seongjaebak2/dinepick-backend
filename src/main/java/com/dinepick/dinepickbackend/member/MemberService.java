package com.dinepick.dinepickbackend.member;

import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.member.dto.MemberResponse;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMyInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        return MemberResponse.from(member);
    }

    public void promoteToAdmin(Long id) {}
}

