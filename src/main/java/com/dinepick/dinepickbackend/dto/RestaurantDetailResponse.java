package com.dinepick.dinepickbackend.dto;

import com.dinepick.dinepickbackend.entity.Category;
import com.dinepick.dinepickbackend.entity.Restaurant;
import com.dinepick.dinepickbackend.entity.RestaurantImage;
import lombok.Getter;

import java.util.List;

@Getter
public class RestaurantDetailResponse {

    private Long id;
    private String name;
    private String address;
    private String description;
    private Category category;
    private int maxPeoplePerReservation;

    private List<String> imageUrls;

    public RestaurantDetailResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.description = restaurant.getDescription();
        this.category = restaurant.getCategory();
        this.maxPeoplePerReservation = restaurant.getMaxPeoplePerReservation();
        this.imageUrls = restaurant.getImages()
                .stream()
                .map(RestaurantImage::getImageUrl)
                .toList();
    }
}