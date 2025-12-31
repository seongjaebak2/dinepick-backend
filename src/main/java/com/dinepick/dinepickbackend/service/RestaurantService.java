package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.dto.RestaurantResponse;
import com.dinepick.dinepickbackend.entity.Restaurant;
import com.dinepick.dinepickbackend.exception.RestaurantNotFoundException;
import com.dinepick.dinepickbackend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    //    레스토랑 목록 + 검색 + 페이징
    public Page<RestaurantResponse> findRestaurants(
            String keyword,
            Pageable pageable
    ) {
        Page<Restaurant> page;

        if (keyword == null || keyword.isBlank()) {
            page = restaurantRepository.findAll(pageable);
        } else {
            page = restaurantRepository.findByNameContaining(keyword, pageable);
        }
        return page.map(RestaurantResponse::from);
    }

    //    레스토랑 상세
    public RestaurantResponse findById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException(restaurantId)
                );

        return RestaurantResponse.from(restaurant);
    }
}
