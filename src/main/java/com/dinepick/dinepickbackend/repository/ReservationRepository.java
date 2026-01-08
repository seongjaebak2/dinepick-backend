package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByMemberEmailOrderByCreatedAtDesc(
            String email,
            Pageable pageable
    );

    boolean existsByRestaurantIdAndReservationDateAndReservationTime(
            Long restaurantId,
            LocalDate reservationDate,
            LocalTime reservationTime
    );

    boolean existsByRestaurantIdAndReservationDateAndReservationTimeAndIdNot(
            Long restaurantId,
            LocalDate reservationDate,
            LocalTime reservationTime,
            Long id
    );
}
