package com.dinepick.dinepickbackend.dto;

import com.dinepick.dinepickbackend.entity.Category;
import com.dinepick.dinepickbackend.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String description;
    private int maxPeoplePerReservation;
    private Category category;
    private Double distance;    // km

    // 일반 API용
    public RestaurantResponse(
            Long id,
            String name,
            String address,
            String description,
            int maxPeoplePerReservation,
            Category category,
            Double distance
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.maxPeoplePerReservation = maxPeoplePerReservation;
        this.category = category;
        this.distance = distance;
    }

    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getMaxPeoplePerReservation(),
                restaurant.getCategory(),
                null
        );
    }

    public static RestaurantResponse from(Restaurant restaurant, Double distance){
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getMaxPeoplePerReservation(),
                restaurant.getCategory(),
                distance
        );
    }

    //  JPQL 전용
    public RestaurantResponse(
        Long id,
        String name,
        String address,
        Category category,
        Double distance
    ){
        this.id=id;
        this.name=name;
        this.address=address;
        this.category=category;
        this.distance=distance;
    }
}
