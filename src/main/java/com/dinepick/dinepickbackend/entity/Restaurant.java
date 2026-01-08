package com.dinepick.dinepickbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    //  위치 정보
    private Double latitude;
    private Double longitude;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int maxPeoplePerReservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public Restaurant(String name, String address, Double latitude, Double longitude, String description, int maxPeoplePerReservation, Category category) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.maxPeoplePerReservation = maxPeoplePerReservation;
        this.category = category;
    }


}
