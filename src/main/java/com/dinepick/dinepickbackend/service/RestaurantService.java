package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.dto.RestaurantResponse;
import com.dinepick.dinepickbackend.entity.Category;
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

    //    레스토랑 목록 + 검색 + 페이징 + 카테고리 검색
    public Page<RestaurantResponse> findRestaurants(
            String keyword,
            Category category,
            Pageable pageable
    ) {
        Page<Restaurant> page;

        if ((keyword == null || keyword.isBlank()) && category == null) {
            page = restaurantRepository.findAll(pageable);
        } else if (keyword != null && category == null) {
            page = restaurantRepository.findByNameContaining(keyword, pageable);
        } else if (keyword == null) {
            page = restaurantRepository.findByCategory(category, pageable);
        } else {
            page = restaurantRepository.findByNameContainingAndCategory(keyword, category, pageable);
        }
        return page.map(RestaurantResponse::from);
    }

    //    레스토랑 상세
    public Restaurant findEntityById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    //  위치 기반 검색
    public Page<RestaurantResponse> findNearby(
            double lat,
            double lng,
            double radius,
            String keyword,
            Category category,
            Pageable pageable
    ) {
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        return restaurantRepository
                .findNearbyWithDistance(lat, lng, radius, keyword, category, pageable)
                ;
    }
}
