package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.dto.RestaurantResponse;
import com.dinepick.dinepickbackend.entity.Category;
import com.dinepick.dinepickbackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContaining(String keyword, Pageable pageable);

    Page<Restaurant> findByCategory(Category category, Pageable pageable);

    Page<Restaurant> findByNameContainingAndCategory(
            String keyword,
            Category category,
            Pageable pageable
    );

    @Query("""
    select new com.dinepick.dinepickbackend.dto.RestaurantResponse(
        r.id,
        r.name,
        r.address,
        r.category,
        (6371.0 * acos(
            cos(radians(:lat)) * cos(radians(r.latitude)) *
            cos(radians(r.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(r.latitude))
        ))
    )
    from Restaurant r
    where r.latitude IS NOT NULL
      and r.longitude IS NOT NULL
      and (6371.0 * acos(
            cos(radians(:lat)) * cos(radians(r.latitude)) *
            cos(radians(r.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(r.latitude))
      )) <= :radius
      and (:category IS NULL OR r.category = :category)
      and (:keyword IS NULL OR r.name LIKE %:keyword%)
    order by
      (6371.0 * acos(
            cos(radians(:lat)) * cos(radians(r.latitude)) *
            cos(radians(r.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(r.latitude))
      )) asc
""")
    Page<RestaurantResponse> findNearbyWithDistance(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius,
            @Param("keyword") String keyword,
            @Param("category") Category category,
            Pageable pageable
    );
}
