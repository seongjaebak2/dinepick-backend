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
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (memberRepository.findByEmail("admin@dinepick.com").isEmpty()) {
            Member admin = new Member(
                    "admin@dinepick.com",
                    passwordEncoder.encode("admin1234"),
                    "관리자",
                    Role.ROLE_ADMIN
            );
            memberRepository.save(admin);
        }

        if (memberRepository.existsByEmail("test@test.com")) {
            return;
        }
        Member member = new Member(
                "test@test.com",
                passwordEncoder.encode("1234"),
                "홍길동",
                Role.ROLE_USER
        );
        memberRepository.save(member);

        Restaurant restaurant = new Restaurant(
                "스시하루",
                "서울 강남구",
                "신선한 스시 전문점",
                6,
                Category.JAPANESE
        );
        restaurantRepository.save(restaurant);

        Reservation reservation = new Reservation(
                member,
                restaurant,
                LocalDate.now(),
                LocalTime.of(18, 0),
                4
        );
        reservationRepository.save(reservation);
    }

}
