package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.ReservationCreateRequest;
import com.dinepick.dinepickbackend.dto.ReservationResponse;
import com.dinepick.dinepickbackend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/availability")
    public Map<String, Object> checkAvailability(
            @RequestParam Long restaurantId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam int peopleCount
    ) {
        boolean available = reservationService
                .isAvailable(restaurantId, date, time, peopleCount);

        return Map.of(
                "available", available,
                "message", available ? "예약 가능합니다." : "예약이 불가능합니다."
        );
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationCreateRequest request
    ) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        ReservationResponse response =
                reservationService.createReservation(email, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{reservationId}")
    public void cancelReservation(
            @PathVariable Long reservationId
    ) {
        reservationService.cancelReservation(reservationId);
    }
}
