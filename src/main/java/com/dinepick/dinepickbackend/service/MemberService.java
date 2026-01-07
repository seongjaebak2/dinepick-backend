package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.dto.MemberResponse;
import com.dinepick.dinepickbackend.exception.auth.UnauthenticatedException;
import com.dinepick.dinepickbackend.exception.member.MemberNotFoundException;
import com.dinepick.dinepickbackend.exception.member.WithdrawnMemberException;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    //회원탈퇴
    @Transactional
    public void withdraw() {
        Member member = getCurrentMember();
        member.withdraw();
    }

    //회원복구
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void restoreMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        if (!member.isDeleted()) {
            throw new IllegalStateException("이미 활성 회원입니다.");
        }
        if (member.getDeletedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new IllegalStateException("복구 가능 기간이 지났습니다.");
        }
        member.restore();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberResponse> findWithdrawnMembers() {

        return memberRepository.findAllByDeletedTrue()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    // === 공통 메서드 ===
    private Member getCurrentMember() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return memberRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(WithdrawnMemberException::new);
    }
}

