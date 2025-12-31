package com.dinepick.dinepickbackend.dto;

import com.dinepick.dinepickbackend.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String description;
    private int maxPeoplePerReservation;

    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getMaxPeoplePerReservation()
        );
    }
}
