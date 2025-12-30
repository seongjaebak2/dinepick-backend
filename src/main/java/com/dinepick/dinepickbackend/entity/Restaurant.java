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

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int maxPeoplePerReservation;

    public Restaurant(String name, String address, String description, int maxPeoplePerReservation) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.maxPeoplePerReservation = maxPeoplePerReservation;
    }


}
