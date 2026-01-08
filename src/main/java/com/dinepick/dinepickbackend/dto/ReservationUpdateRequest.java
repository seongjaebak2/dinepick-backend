package com.dinepick.dinepickbackend.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationUpdateRequest {
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int peopleCount;
}
