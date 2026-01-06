package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.entity.Category;
import com.dinepick.dinepickbackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContaining(String keyword, Pageable pageable);

    Page<Restaurant> findByCategory(Category category, Pageable pageable);

    Page<Restaurant> findByNameContainingAndCategory(
            String keyword,
            Category category,
            Pageable pageable
    );
}
