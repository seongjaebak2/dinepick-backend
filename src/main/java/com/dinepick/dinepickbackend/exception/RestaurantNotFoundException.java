package com.dinepick.dinepickbackend.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("레스토랑을 찾을 수 없습니다. id=" + id);
    }
}
