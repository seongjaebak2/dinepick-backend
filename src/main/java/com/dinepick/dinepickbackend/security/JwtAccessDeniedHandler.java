package com.dinepick.dinepickbackend.security;

import com.dinepick.dinepickbackend.exception.ErrorResponse;
import com.dinepick.dinepickbackend.exception.auth.AccessDeniedException;
import com.dinepick.dinepickbackend.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * 권한이 없는 리소스에 접근했을 때 실행되는 핸들러
     * - Security 필터 체인에서 AccessDeniedException이 발생하면 이 메서드가 호출됨
     */
    @Override
    public void handle(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.access.@NonNull AccessDeniedException ex
    ) throws IOException {
        // 1. 미리 정의해둔 커스텀 예외(AccessDeniedException) 생성
        AuthException authException = new AccessDeniedException();

        // 2. HTTP 응답 헤더 설정 (상태 코드 및 JSON 형식 지정)
        response.setStatus(authException.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        // 3. 일관된 에러 형식을 위한 ErrorResponse 객체 생성
        ErrorResponse errorResponse = new ErrorResponse(
                authException.getStatus().value(),
                authException.getErrorCode(),
                authException.getMessage()
        );

        // 4. ObjectMapper를 이용해 DTO를 JSON 문자열로 변환하여 응답 바디에 작성
        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}

