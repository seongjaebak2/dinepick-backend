package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.dto.RestaurantDetailResponse;
import com.dinepick.dinepickbackend.dto.RestaurantResponse;
import com.dinepick.dinepickbackend.entity.Category;
import com.dinepick.dinepickbackend.entity.Restaurant;
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
    public RestaurantDetailResponse getRestaurant(
            @PathVariable Long restaurantId
    ) {
        Restaurant restaurant = restaurantService.findEntityById(restaurantId);
        return new RestaurantDetailResponse(restaurant);
    }

    // 위치 기반 검색
    // - 거리 계산은 서버에서 수행
    // - 결과는 거리 오름차순으로 고정 정렬
    @GetMapping("/nearby")
    public Page<RestaurantResponse> nearbyRestaurants(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            Pageable pageable
    ) {
        return restaurantService.findNearby(
                lat, lng, radius, keyword, category, pageable
        );
    }
}
