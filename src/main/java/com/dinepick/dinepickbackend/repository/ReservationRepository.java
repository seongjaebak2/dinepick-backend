package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByRestaurantIdAndReservationDateAndReservationTime(
            Long restaurantId,
            LocalDate reservationDate,
            LocalTime reservationTime
    );
}
