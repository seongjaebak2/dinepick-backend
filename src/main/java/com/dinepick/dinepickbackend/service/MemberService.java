package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.dto.MemberResponse;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 로그인한 사용자 정보 조회
     */
    public MemberResponse getMyInfo() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        return MemberResponse.from(member);
    }

    /**
     * 전체 회원 조회 (ADMIN)
     */
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    /**
     * 회원 단건 조회 (ADMIN)
     */
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        return MemberResponse.from(member);
    }

    /**
     * 회원 삭제 (ADMIN)
     */
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}

