package com.dinepick.dinepickbackend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody Map<String, String> req
    ) {
        authService.signup(
                req.get("email"),
                req.get("password"),
                req.get("name")
        );
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> req
    ) {
        String token = authService.login(
                req.get("email"),
                req.get("password")
        );
        return ResponseEntity.ok(
                Map.of("accessToken", token)
        );
    }
}
