package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.ReservationCreateRequest;
import com.dinepick.dinepickbackend.dto.ReservationResponse;
import com.dinepick.dinepickbackend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticationPrincipal String email,
            @RequestBody ReservationCreateRequest request
    ) {
        ReservationResponse response =
                reservationService.createReservation(email, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public void cancelReservation(
            @PathVariable Long reservationId
    ){
        reservationService.cancelReservation(reservationId);
    }
}
