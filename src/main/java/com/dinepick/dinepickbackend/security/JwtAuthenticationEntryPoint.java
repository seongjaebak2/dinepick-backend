package com.dinepick.dinepickbackend.security;

import com.dinepick.dinepickbackend.exception.ErrorResponse;
import com.dinepick.dinepickbackend.exception.auth.AuthException;
import com.dinepick.dinepickbackend.exception.auth.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex
    ) throws IOException {
        // 인증 실패 시 기본적으로 InvalidTokenException 정보를 반환
        AuthException authException = new InvalidTokenException();

        response.setStatus(authException.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                authException.getStatus().value(),
                authException.getErrorCode(),
                authException.getMessage()
        );

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}

