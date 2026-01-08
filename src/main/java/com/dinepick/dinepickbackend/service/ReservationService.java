package com.dinepick.dinepickbackend.service;

import com.dinepick.dinepickbackend.dto.MyReservationResponse;
import com.dinepick.dinepickbackend.dto.ReservationCreateRequest;
import com.dinepick.dinepickbackend.dto.ReservationResponse;
import com.dinepick.dinepickbackend.dto.ReservationUpdateRequest;
import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.entity.Reservation;
import com.dinepick.dinepickbackend.entity.Restaurant;
import com.dinepick.dinepickbackend.entity.Role;
import com.dinepick.dinepickbackend.exception.ReservationNotFoundException;
import com.dinepick.dinepickbackend.exception.UnauthorizedReservationAccessException;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import com.dinepick.dinepickbackend.repository.ReservationRepository;
import com.dinepick.dinepickbackend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    //  예약 가능 여부 확인
    public boolean isAvailable(
            Long restaurantId,
            LocalDate date,
            LocalTime time,
            int peopleCount
    ) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new IllegalArgumentException("레스토랑을 찾을 수 없습니다.")
                );
        if (peopleCount > restaurant.getMaxPeoplePerReservation()) {
            return false;
        }

        return !reservationRepository
                .existsByRestaurantIdAndReservationDateAndReservationTime(
                        restaurantId, date, time
                );
    }

    //  예약 생성
    public ReservationResponse createReservation(String email, ReservationCreateRequest request) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을 수 없습니다."));

        //        중복 예약 체크
        boolean exists = reservationRepository.existsByRestaurantIdAndReservationDateAndReservationTime(restaurant.getId(), request.getReservationDate(), request.getReservationTime());

        if (exists) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }

        //        인원 수 제한 체크
        if (request.getPeopleCount() > restaurant.getMaxPeoplePerReservation()) {
            throw new IllegalArgumentException("예약 가능 인원을 초과했습니다.");
        }

        Reservation reservation = new Reservation(member, restaurant, request.getReservationDate(), request.getReservationTime(), request.getPeopleCount());

        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    // 본인 예약 조회
    @Transactional(readOnly = true)
    public Page<MyReservationResponse> getMyReservations(Pageable pageable) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return reservationRepository
                .findByMemberEmailOrderByCreatedAtDesc(email, pageable)
                .map(MyReservationResponse::from);
    }

    //  예약 수정
    public ReservationResponse updateReservation(
            Long reservationId,
            ReservationUpdateRequest request
    ) {
        // 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        // 현재 로그인 사용자
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        // 본인 예약인지 검증
        if (!reservation.getMember().getId().equals(member.getId()) && !member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UnauthorizedReservationAccessException();
        }

        Restaurant restaurant = reservation.getRestaurant();

        // 인원 수 제한 체크
        if (request.getPeopleCount() > restaurant.getMaxPeoplePerReservation()) {
            throw new IllegalArgumentException("예약 가능 인원을 초과했습니다.");
        }

        // 중복 예약 체크 (본인 예약 제외)
        boolean exists = reservationRepository
                .existsByRestaurantIdAndReservationDateAndReservationTimeAndIdNot(
                        restaurant.getId(),
                        request.getReservationDate(),
                        request.getReservationTime(),
                        reservationId
                );

        if (exists) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }

        //예약 수정
        reservation.update(
                request.getReservationDate(),
                request.getReservationTime(),
                request.getPeopleCount()
        );

        return ReservationResponse.from(reservation);
    }

    //  예약 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(reservationId)
                );

        //        현재 로그인 사용자 이메일
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        //        본인 및 관리자 계정인지 검증
        if (!reservation.getMember().getId().equals(member.getId()) && !member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UnauthorizedReservationAccessException();
        }

        reservationRepository.delete(reservation);
    }


}
