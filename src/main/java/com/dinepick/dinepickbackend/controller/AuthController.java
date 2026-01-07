package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.*;
import com.dinepick.dinepickbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestBody SignupRequest request
    ) {
        authService.signup(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
        return ResponseEntity.ok().build();
    }

    //로그인 API (Access + Refresh Token 발급)
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request
    ) {
        TokenResponse tokenResponse =
                authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(tokenResponse);
    }

    //Access token 재발급
    @PostMapping("/reissue")
    public AccessTokenResponse reissue(
            @RequestBody RefreshTokenRequest request
    ) {
        String accessToken =
                authService.reissueAccessToken(request.getRefreshToken());
        return new AccessTokenResponse(accessToken);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }


}
