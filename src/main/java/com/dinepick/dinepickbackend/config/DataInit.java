package com.dinepick.dinepickbackend.config;

import com.dinepick.dinepickbackend.entity.*;
import com.dinepick.dinepickbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ===== 회원 초기화 =====
        if (!memberRepository.existsByEmail("admin@dinepick.com")) {
            memberRepository.save(
                    new Member(
                            "admin@dinepick.com",
                            passwordEncoder.encode("admin1234"),
                            "관리자",
                            Role.ROLE_ADMIN
                    )
            );
        }

        if (!memberRepository.existsByEmail("test@test.com")) {
            memberRepository.save(
                    new Member(
                            "test@test.com",
                            passwordEncoder.encode("1234"),
                            "홍길동",
                            Role.ROLE_USER
                    )
            );
        }

        // ===== 식당 샘플 데이터 =====
        saveRestaurantIfNotExists(
                "톤쇼우 서면점",
                "부산 부산진구 중앙대로 672",
                35.1569,
                129.0588,
                "프리미엄 돈카츠 전문점",
                10,
                Category.JAPANESE
        );

        saveRestaurantIfNotExists(
                "송정3대국밥 서면점",
                "부산 부산진구 중앙대로 691",
                35.1574,
                129.0592,
                "부산 대표 돼지국밥",
                15,
                Category.KOREAN
        );

        saveRestaurantIfNotExists(
                "개미집 서면점",
                "부산 부산진구 중앙대로 702",
                35.1578,
                129.0599,
                "낙곱새 원조 맛집",
                12,
                Category.KOREAN
        );

        saveRestaurantIfNotExists(
                "버거샵 서면",
                "부산 부산진구 중앙대로 709",
                35.1581,
                129.0603,
                "수제버거 전문점",
                8,
                Category.WESTERN
        );

        saveRestaurantIfNotExists(
                "카페 이드",
                "부산 부산진구 중앙대로 710",
                35.1583,
                129.0606,
                "디저트 & 스페셜티 커피",
                20,
                Category.CAFE
        );
    }
    private void saveRestaurantIfNotExists(
            String name,
            String address,
            double lat,
            double lng,
            String description,
            int capacity,
            Category category
    ) {
        if (restaurantRepository.existsByNameAndAddress(name, address)) {
            return;
        }

        Restaurant restaurant = new Restaurant(
                name,
                address,
                lat,
                lng,
                description,
                capacity,
                category
        );

        // ===== 이미지 샘플 =====
        if (name.equals("톤쇼우 서면점")) {
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1604909052743-94e838986d24",
                    true
            ));
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe",
                    false
            ));
        }

        if (name.equals("송정3대국밥 서면점")) {
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1604908177522-4324c5f1e7a6",
                    true
            ));
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1626078297428-69a3bdfb1efb",
                    false
            ));
        }

        if (name.equals("개미집 서면점")) {
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1600891964599-f61ba0e24092",
                    true
            ));
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1553621042-f6e147245754",
                    false
            ));
        }

        if (name.equals("버거샵 서면")) {
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1550547660-d9450f859349",
                    true
            ));
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1568901346375-23c9450c58cd",
                    false
            ));
        }

        if (name.equals("카페 이드")) {
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1509042239860-f550ce710b93",
                    true
            ));
            restaurant.addImage(new RestaurantImage(
                    "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085",
                    false
            ));
        }

        restaurantRepository.save(restaurant);
    }
}
