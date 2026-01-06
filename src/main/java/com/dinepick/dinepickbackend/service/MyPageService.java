package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.dto.MyReservationResponse;
import com.dinepick.dinepickbackend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {
    private final ReservationRepository reservationRepository;

    public Page<MyReservationResponse> getMyReservations(
            String email,
            Pageable pageable
    ) {
        return reservationRepository
                .findByMemberEmailOrderByCreatedAtDesc(email, pageable)
                .map(MyReservationResponse::from);
    }
}
