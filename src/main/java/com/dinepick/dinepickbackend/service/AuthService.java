package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.exception.auth.AuthException;
import com.dinepick.dinepickbackend.security.JwtTokenProvider;
import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.entity.Role;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * 로그인 처리
     * @param email  로그인 이메일
     * @param password  로그인 비밀번호
     * @return JWT Access Token
     */
    public String login(String email, String password) {

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

        // 로그인 성공 시 JWT 토큰 생성 및 반환
        return jwtTokenProvider.createToken(
                member.getEmail(), // 토큰 subject
                member.getRole()   // 권한 정보
        );
    }
}
