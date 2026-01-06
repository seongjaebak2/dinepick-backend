package com.dinepick.dinepickbackend.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationCreateRequest {
    private Long restaurantId;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int peopleCount;
}
