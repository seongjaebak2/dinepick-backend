package com.dinepick.dinepickbackend.dto;

import com.dinepick.dinepickbackend.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private String restaurantName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int peopleCount;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRestaurant().getName(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getPeopleCount()
        );
    }
}
