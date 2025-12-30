package com.dinepick.dinepickbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "restaurant_id",
                                "reservation_date",
                                "reservation_time"
                        }
                )
        }
)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    예약자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //    예약 레스토랑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @Column(nullable = false)
    private int peopleCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public Reservation(
            Member member,
            Restaurant restaurant,
            LocalDate reservationDate,
            LocalTime reservationTime,
            int peopleCount
    ) {
        this.member = member;
        this.restaurant = restaurant;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.peopleCount = peopleCount;
    }

}
