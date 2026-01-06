package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.RestaurantResponse;
import com.dinepick.dinepickbackend.entity.Category;
import com.dinepick.dinepickbackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    //    레스토랑 목록 + 검색
    @GetMapping
    public Page<RestaurantResponse> getRestaurants(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            Pageable pageable
    ) {
        return restaurantService.findRestaurants(keyword, category, pageable);
    }

    //    레스토랑 상세
    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurant(
            @PathVariable Long restaurantId
    ) {
        return restaurantService.findById(restaurantId);
    }
}
