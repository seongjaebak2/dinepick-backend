package com.dinepick.dinepickbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 액세스 토큰 전용 응답 DTO
 * - 리프레시 토큰을 통해 액세스 토큰을 갱신했을 때 클라이언트에 전달함
 */
@Getter
@AllArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
}

