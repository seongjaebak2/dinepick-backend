package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 인증 관련 API를 담당하는 Controller
 * - 회원가입
 * - 로그인(JWT 토큰 발급)
 */
@RestController // REST API 컨트롤러임을 명시
@RequestMapping("/api/auth") // 인증 관련 공통 URL
@RequiredArgsConstructor // final 필드를 생성자로 자동 주입
public class AuthController {

    // 인증 비즈니스 로직을 처리하는 Service
    private final AuthService authService;

    /**
     * 회원가입 API
     * @param req 클라이언트에서 전달한 회원 정보(email, password, name)
     * @return 회원가입 성공 메시지
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody Map<String, String> req
    ) {
        // Service 계층에 회원가입 처리 위임
        authService.signup(
                req.get("email"),
                req.get("password"),
                req.get("name")
        );

        // 성공 시 HTTP 200 OK 응답 반환
        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 로그인 API
     * @param req 클라이언트에서 전달한 로그인 정보(email, password)
     * @return JWT Access Token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> req
    ) {
        // 로그인 성공 시 JWT 토큰 발급
        String token = authService.login(
                req.get("email"),
                req.get("password")
        );

        // 토큰을 JSON 형태로 반환
        return ResponseEntity.ok(
                Map.of("accessToken", token)
        );
    }
}
