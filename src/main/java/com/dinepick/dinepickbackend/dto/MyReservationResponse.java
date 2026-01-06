package com.dinepick.dinepickbackend.dto;

import com.dinepick.dinepickbackend.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class MyReservationResponse {
    private Long reservationId;
    private String restaurantName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int peopleCount;
    private LocalDateTime createdAt;

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getRestaurant().getName(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getPeopleCount(),
                reservation.getCreatedAt()
        );
    }
}
