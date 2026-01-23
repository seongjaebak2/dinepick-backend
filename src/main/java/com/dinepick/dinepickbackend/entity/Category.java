package com.dinepick.dinepickbackend.entity;

public enum Category {
    KOREAN,
    JAPANESE,
    CHINESE,
    WESTERN,
    CAFE,
    ETC;

    // 카카오 카테고리 → 내부 Category 변환
    public static Category fromKakao(String kakaoCategory) {
        if (kakaoCategory == null) return ETC;

        if (kakaoCategory.contains("카페")) return CAFE;
        if (kakaoCategory.contains("한식")) return KOREAN;
        if (kakaoCategory.contains("일식")) return JAPANESE;
        if (kakaoCategory.contains("중식")) return CHINESE;
        if (kakaoCategory.contains("양식")) return WESTERN;

        return ETC;
    }
}
