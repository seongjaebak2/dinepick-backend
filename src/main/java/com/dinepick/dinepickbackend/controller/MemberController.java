package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.MemberResponse;
import com.dinepick.dinepickbackend.dto.MemberUpdateRequest;
import com.dinepick.dinepickbackend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //내 정보 조회 (로그인한 사용자)
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberResponse> getMyInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    // 회원정보 수정 (로그인한 사용자)
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberResponse> updateMyInfo(
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        return ResponseEntity.ok(memberService.updateMyInfo(request));
    }

    //회원 탈퇴 (로그인한 사용자)
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> withdraw() {
        memberService.withdraw();
        return ResponseEntity.noContent().build(); // 204 반환
    }

    //전체 회원 조회 (관리자 전용)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    //회원 단건 조회 (관리자 전용)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }

    //회원 복구 (관리자 전용)
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        memberService.restoreMember(id);
        return ResponseEntity.ok().build();
    }

    //탈퇴 회원 목록 조회 (관리자 전용)
    @GetMapping("/withdrawn")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberResponse>> findWithdrawnMembers() {
        return ResponseEntity.ok(memberService.findWithdrawnMembers());
    }
}
