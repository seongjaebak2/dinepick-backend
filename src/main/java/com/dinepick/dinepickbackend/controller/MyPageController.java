package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.MyReservationResponse;
import com.dinepick.dinepickbackend.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/reservations")
    public Page<MyReservationResponse> getMyReservations(
            @AuthenticationPrincipal String email,
            Pageable pageable
    ) {
        return myPageService.getMyReservations(email, pageable);
    }
}
