package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.dto.MemberResponse;
import com.dinepick.dinepickbackend.exception.auth.UnauthenticatedException;
import com.dinepick.dinepickbackend.exception.member.MemberNotFoundException;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 로그인한 사용자 정보 조회
     */
    public MemberResponse getMyInfo() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthenticatedException();
        }

        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponse.from(member);
    }

    /**
     * 전체 회원 조회 (ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    /**
     * 회원 단건 조회 (ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public MemberResponse findById(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::from)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 회원 삭제 (ADMIN)
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }
}

