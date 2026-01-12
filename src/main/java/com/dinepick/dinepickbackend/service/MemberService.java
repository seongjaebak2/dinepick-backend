package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.dto.MemberUpdateRequest;
import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.dto.MemberResponse;
import com.dinepick.dinepickbackend.exception.auth.AuthException;
import com.dinepick.dinepickbackend.exception.auth.UnauthenticatedException;
import com.dinepick.dinepickbackend.exception.member.MemberNotFoundException;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import com.dinepick.dinepickbackend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    // 공통 메서드
    private Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 객체 자체가 없거나 익명 사용자인 경우 체크
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthenticatedException();
        }
        String email = authentication.getName();
        // 탈퇴하지 않은 회원만 조회
        return memberRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    //로그인한 사용자 정보 조회
    public MemberResponse getMyInfo() {
        return MemberResponse.from(getCurrentMember());
    }

    //회원 정보 업데이트
    @Transactional
    public void updateMyInfo(MemberUpdateRequest request) {
        Member member = getCurrentMember();

        // 1️⃣ 기본 정보 수정
        member.updateName(request.getName());

        // 2️⃣ 비밀번호 변경 여부 판단
        boolean wantsPasswordChange =
                request.getCurrentPassword() != null ||
                        request.getNewPassword() != null ||
                        request.getNewPasswordConfirm() != null;

        if (wantsPasswordChange) {
            // 2-1 필수값 검증
            if (request.getCurrentPassword() == null ||
                    request.getNewPassword() == null ||
                    request.getNewPasswordConfirm() == null) {
                throw new AuthException(
                        HttpStatus.BAD_REQUEST,
                        "PASSWORD_REQUIRED",
                        "비밀번호 변경 정보를 모두 입력해주세요."
                );
            }

            // 2-2 현재 비밀번호 확인
            if (!passwordEncoder.matches(
                    request.getCurrentPassword(),
                    member.getPassword()
            )) {
                throw new AuthException(
                        HttpStatus.UNAUTHORIZED,
                        "INVALID_PASSWORD",
                        "현재 비밀번호가 일치하지 않습니다."
                );
            }

            // 2-3 새 비밀번호 확인
            if (!request.getNewPassword()
                    .equals(request.getNewPasswordConfirm())) {
                throw new AuthException(
                        HttpStatus.BAD_REQUEST,
                        "PASSWORD_MISMATCH",
                        "새 비밀번호가 일치하지 않습니다."
                );
            }

            // 2-4 동일 비밀번호 방지
            if (passwordEncoder.matches(
                    request.getNewPassword(),
                    member.getPassword()
            )) {
                throw new AuthException(
                        HttpStatus.BAD_REQUEST,
                        "SAME_PASSWORD",
                        "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."
                );
            }

            // 2-5 비밀번호 변경
            member.changePassword(
                    passwordEncoder.encode(request.getNewPassword())
            );
        }
    }

    //전체 회원 조회 (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    //회원 단건 조회 (ADMIN)
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
        refreshTokenRepository.deleteByMemberId(member.getId());
    }

    //회원복구 (ADMIN)
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void restoreMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        if (!member.isDeleted()) {
            throw new IllegalStateException("이미 활성 회원입니다.");
        }
        if (member.getDeletedAt() != null &&
                member.getDeletedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new IllegalStateException("복구 가능 기간(7일)이 지났습니다.");
        }
        member.restore();
    }

    //탈퇴회원 조회 (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberResponse> findWithdrawnMembers() {

        return memberRepository.findAllByDeletedTrue()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}

