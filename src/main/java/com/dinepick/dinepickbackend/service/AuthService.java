package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.dto.TokenResponse;
import com.dinepick.dinepickbackend.entity.MemberStatus;
import com.dinepick.dinepickbackend.entity.RefreshToken;
import com.dinepick.dinepickbackend.exception.auth.AuthException;
import com.dinepick.dinepickbackend.exception.member.WithdrawnMemberException;
import com.dinepick.dinepickbackend.repository.RefreshTokenRepository;
import com.dinepick.dinepickbackend.security.JwtTokenProvider;
import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.entity.Role;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

/**
 * 인증(Auth) 관련 비즈니스 로직을 처리하는 Service
 * - 회원가입 처리
 * - 로그인 검증 및 JWT 토큰 발급
 */
@Service // 비즈니스 로직을 담당하는 Service 계층
@RequiredArgsConstructor // final 필드 생성자 주입
public class AuthService {

    // 회원 정보 DB 접근을 위한 Repository
    private final MemberRepository memberRepository;

    // JWT 토큰 생성 및 검증을 담당하는 Provider
    private final JwtTokenProvider jwtTokenProvider;

    // 비밀번호 암호화를 위한 PasswordEncoder
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원가입 처리
     * @param email  회원 이메일 (아이디)
     * @param password  회원 비밀번호 (평문 → 암호화)
     * @param name  회원 이름
     */
    public void signup(String email, String password, String name) {

        // 이미 가입된 이메일인지 중복 체크
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new AuthException(
                    HttpStatus.BAD_REQUEST,
                    "DUPLICATE_EMAIL",
                    "이미 가입된 이메일입니다."
            );
        }

        // 회원 엔티티 생성
        // 비밀번호는 반드시 암호화하여 저장
        Member member = new Member(
                email,                          // 이메일
                passwordEncoder.encode(password), // 암호화된 비밀번호
                name,                           // 이름
                Role.ROLE_USER                  // 기본 권한: USER
        );

        // DB에 회원 정보 저장
        memberRepository.save(member);
    }

    /**
     * 로그인 (Access + Refresh 발급)
     */
    public TokenResponse login(String email, String password) {
        // 이메일로 회원 조회 (없으면 예외 발생)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(
                        HttpStatus.NOT_FOUND,
                        "MEMBER_NOT_FOUND",
                        "존재하지 않는 회원입니다."
                ));
        // 입력한 비밀번호와 저장된 암호화 비밀번호 비교
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_PASSWORD",
                    "비밀번호가 일치하지 않습니다."
            );
        }
        //회원탈퇴 여부
        if (member.isDeleted() || member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new WithdrawnMemberException();
        }
        // 기존 Refresh Token 제거 (단일 로그인 정책)
        refreshTokenRepository.deleteByMemberId(member.getId());

        String accessToken =
                jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());

        String refreshToken =
                jwtTokenProvider.createRefreshToken(member.getEmail());

        refreshTokenRepository.save(
                new RefreshToken(
                        refreshToken,
                        member,
                        LocalDateTime.now().plusDays(7)
                )
        );

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * Access Token 재발급
     */
    public String reissueAccessToken(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException(
                        HttpStatus.UNAUTHORIZED,
                        "INVALID_REFRESH_TOKEN",
                        "유효하지 않은 Refresh Token입니다."
                ));

        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "EXPIRED_REFRESH_TOKEN",
                    "Refresh Token이 만료되었습니다."
            );
        }

        Member member = token.getMember();

        // ✅ 탈퇴 / 정지 회원 재발급 차단
        if (member.isDeleted() || member.getStatus() == MemberStatus.WITHDRAWN) {
            refreshTokenRepository.delete(token);
            throw new WithdrawnMemberException();
        }

        return jwtTokenProvider.createAccessToken(
                member.getEmail(),
                member.getRole()
        );
    }

    /**
     * 로그아웃
     */
    public void logout(@RequestBody String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) return;

        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}

