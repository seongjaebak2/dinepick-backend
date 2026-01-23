package com.dinepick.dinepickbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RestaurantImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String imageUrl;

    // 대표 이미지 여부
    @Column(nullable = false)
    private boolean isThumbnail;

    public RestaurantImage(Restaurant restaurant, String imageUrl, boolean isThumbnail) {
        this.restaurant = restaurant;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
    }

    void setRestaurant(Restaurant restaurant){
        this.restaurant = restaurant;
    }

}
