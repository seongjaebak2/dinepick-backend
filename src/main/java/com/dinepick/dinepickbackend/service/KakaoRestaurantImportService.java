package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.entity.*;
import com.dinepick.dinepickbackend.kakao.KakaoMapService;
import com.dinepick.dinepickbackend.kakao.KakaoPlaceResponse;
import com.dinepick.dinepickbackend.repository.RestaurantImageRepository;
import com.dinepick.dinepickbackend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoRestaurantImportService {

    private final KakaoMapService kakaoMapService;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;

    @Transactional
    public int importByKeyword(String keyword) {

        int page = 1;
        int savedCount = 0;

        while (page <= 45) {
            KakaoPlaceResponse response = kakaoMapService.search(keyword, page);

            if (response == null || response.getDocuments().isEmpty()) {
                break;
            }

            for (var doc : response.getDocuments()) {

                if (restaurantRepository.existsByNameAndAddress(
                        doc.getPlace_name(),
                        doc.getAddress_name()
                )) continue;

                Restaurant restaurant = restaurantRepository.save(
                        new Restaurant(
                                doc.getPlace_name(),
                                doc.getAddress_name(),
                                Double.parseDouble(doc.getY()),
                                Double.parseDouble(doc.getX()),
                                null,
                                10,
                                Category.fromKakao(doc.getCategory_name())
                        )
                );

                // 썸네일 이미지 (임시)
                restaurantImageRepository.save(
                        new RestaurantImage(
                                restaurant,
                                randomImageUrl(),
                                true
                        )
                );

                savedCount++;
            }
            if (response.getMeta().isEnd()){
                break;
            }

            page++;
        }
        return savedCount;
    }

    private String randomImageUrl() {
        return "https://images.unsplash.com/photo-1600891964599-f61ba0e24092";
    }
}
