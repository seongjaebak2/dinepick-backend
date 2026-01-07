package com.dinepick.dinepickbackend.exception;

public class UnauthorizedReservationAccessException extends RuntimeException {
    public UnauthorizedReservationAccessException() {
        super("본인의 예약만 취소할 수 있습니다.");
    }
}
