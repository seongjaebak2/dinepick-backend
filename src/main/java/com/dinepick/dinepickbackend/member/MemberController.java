package com.dinepick.dinepickbackend.member;

import com.dinepick.dinepickbackend.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public MemberResponse me(Authentication authentication) {
        String email = authentication.getName();
        return memberService.getMyInfo(email);
    }
}
