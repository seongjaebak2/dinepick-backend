package com.dinepick.dinepickbackend.config;

import com.dinepick.dinepickbackend.security.JwtAccessDeniedHandler;
import com.dinepick.dinepickbackend.security.JwtAuthenticationEntryPoint;
import com.dinepick.dinepickbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 전체 설정 클래스
 * - JWT 기반 인증 방식 적용
 * - 세션 미사용 (STATELESS)
 * - 인증/인가 규칙 정의
 */
@EnableMethodSecurity // @PreAuthorize 사용 가능
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 토큰 생성/검증 Provider
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 비밀번호 암호화 Bean
     * - 회원가입 시 비밀번호 암호화
     * - 로그인 시 비밀번호 비교
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security Filter Chain 설정
     * - 들어오는 모든 HTTP 요청에 대한 보안 필터 적용 순서와 규칙을 정의함
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF 보안 비활성화: REST API 서버이며 JWT(Stateless)를 사용하므로 쿠키 기반의 CSRF 공격 위험이 낮음
                .csrf(csrf -> csrf.disable())
                // 2. CORS 설정 적용: 외부 도메인(React 등)에서의 API 호출 허용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 3. 세션 관리 정책 설정: JWT를 사용하므로 서버에서 세션을 생성하거나 유지하지 않음(STATELESS)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 4. 보안 예외 처리기 등록: 인증 실패(401)와 인가 거부(403) 시 실행될 커스텀 핸들러 지정
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint) //401에러 처리기
                        .accessDeniedHandler(accessDeniedHandler) // 403 에러 처리기
                )
                // 5. URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 특정 경로는 인증 없이도 접근 가능 (로그인, 회원가입, 식당 조회 등)
                        .requestMatchers("/api/auth/**","/api/restaurants/**").permitAll()
                        // 그 외 모든 요청은 반드시 로그인(인증)이 필요함
                        .anyRequest().authenticated()
                )
                // 6. JWT 인증 필터 위치 지정: 사용자 이름/비밀번호 확인 필터 전에 JWT 토큰 검사 필터를 먼저 실행
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    /**
     * CORS 설정
     * - 프론트엔드와 백엔드가 서로 다른 도메인(포트)일 때 통신을 가능하게 함
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 주소 허용
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // 허용 HTTP 메서드
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        // 모든 헤더 허용 (Authorization 등)
        configuration.setAllowedHeaders(List.of("*"));
        // 쿠키 및 인증 정보 포함 허용 여부
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용

        return source;
    }
}
