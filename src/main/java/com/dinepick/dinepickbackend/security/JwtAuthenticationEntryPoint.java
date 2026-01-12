package com.dinepick.dinepickbackend.security;

import com.dinepick.dinepickbackend.exception.ErrorResponse;
import com.dinepick.dinepickbackend.exception.auth.AuthException;
import com.dinepick.dinepickbackend.exception.auth.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 사용자가 인증되지 않았을 때(로그인 필요) 실행되는 메서드
     * - 유효하지 않은 토큰이거나 토큰 없이 접근할 때 401 에러를 JSON 형태로 반환함
     */
    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AuthenticationException ex
    ) throws IOException {
        // 1. 인증 실패를 나타내는 커스텀 예외(InvalidTokenException) 생성
        AuthException authException = new InvalidTokenException();

        // 2. HTTP 응답 상태 코드(401) 및 콘텐츠 타입 설정
        response.setStatus(authException.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        // 3. 클라이언트에게 전달할 일관된 에러 응답 객체 구성
        ErrorResponse errorResponse = new ErrorResponse(
                authException.getStatus().value(),
                authException.getErrorCode(),
                authException.getMessage()
        );

        // 4. JSON 변환 후 응답 바디에 출력
        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}

