package com.dinepick.dinepickbackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test(Authentication authentication) {
        return "인증 성공: " + authentication.getName();
    }

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.getName(); // email
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/test")
    public String adminOnly() {
        return "관리자만 접근 가능";
    }
}

