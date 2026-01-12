package com.dinepick.dinepickbackend.security;

import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 인증 필터
 * - 모든 요청마다 JWT 토큰을 검사
 * - 토큰이 유효하면 Spring Security 인증 객체를 생성
 */
@Slf4j //로그 기록용
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 토큰 생성/검증을 담당하는 Provider
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    /**
     * HTTP 요청이 들어올 때마다 한 번씩 실행되는 필터
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");

        // 헤더가 존재하고 "Bearer "로 시작하는 경우만 처리
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // "Bearer " 이후의 실제 JWT 토큰 값
            String token = authHeader.substring(7);

            try {
                // 1️⃣ 토큰 유효성 검증
                if (jwtTokenProvider.validateToken(token)) {

                    // 2️⃣ JWT 토큰에서 사용자 정보 추출
                    String email = jwtTokenProvider.getEmail(token);
                    String role = jwtTokenProvider.getRole(token);

                    // 3️⃣ DB 조회 + 탈퇴 여부 확인
                    Member member = memberRepository.findByEmail(email)
                            .orElse(null);

                    if (member != null && !member.isDeleted()) {
                        /* 4️⃣
                         * Spring Security 인증 객체 생성
                         * - principal: 사용자 식별 정보 (email)
                         * - credentials: null (JWT 방식이므로 비밀번호 사용 안 함)
                         * - authorities: 사용자 권한
                         */
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        List.of(new SimpleGrantedAuthority(role))
                                );
                        // 요청 정보(IP, 세션 등) 설정
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        // SecurityContext에 인증 정보 저장
                        // → 이후 컨트롤러에서 인증된 사용자로 인식됨
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 인증 정보 제거
                log.error("JWT 인증 실패: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
