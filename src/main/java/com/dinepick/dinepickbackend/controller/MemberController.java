package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.MemberResponse;
import com.dinepick.dinepickbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 내 정보 조회 (로그인한 사용자)
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public MemberResponse getMyInfo() {
        return memberService.getMyInfo();
    }

    /**
     * 전체 회원 조회 (관리자 전용)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberResponse> findAll() {
        return memberService.findAll();
    }

    /**
     * 회원 단건 조회 (관리자 전용)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MemberResponse findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    /**
     * 회원 삭제 (관리자 전용)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
