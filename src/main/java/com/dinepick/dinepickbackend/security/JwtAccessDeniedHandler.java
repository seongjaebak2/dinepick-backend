package com.dinepick.dinepickbackend.security;

import com.dinepick.dinepickbackend.exception.ErrorResponse;
import com.dinepick.dinepickbackend.exception.auth.AccessDeniedException;
import com.dinepick.dinepickbackend.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException ex
    ) throws IOException {

        AuthException authException = new AccessDeniedException();

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

