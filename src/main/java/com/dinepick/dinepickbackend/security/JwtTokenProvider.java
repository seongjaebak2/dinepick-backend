package com.dinepick.dinepickbackend.security;

import com.dinepick.dinepickbackend.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스
 * - Access Token 발급
 * - 토큰에서 사용자 정보(email, role) 추출
 * - 토큰 유효성 검증
 */
@Component
public class JwtTokenProvider {
    /**
     * JWT 서명에 사용되는 비밀키
     * ⚠ 실제 서비스에서는 application.yml 또는 환경변수로 관리해야 함
     */
    private final Key key;
    /**
     * 토큰 만료 시간 (1시간)
     * 1000ms * 60초 * 30분
     */
    private static final long ACCESS_EXPIRE = 1000 * 60; // 30분
    private static final long REFRESH_EXPIRE = 1000L * 60 * 60 * 24 * 7; // 7일

    public JwtTokenProvider() {
        String secret = "dinepick-secret-key-dinepick-secret-key";
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(String email, Role role) {
        return createToken(email, role, ACCESS_EXPIRE);
    }

    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT Access Token 생성
     *
     * @param email 사용자 이메일 (식별자)
     * @param role  사용자 권한 (ROLE_USER, ROLE_ADMIN)
     * @return 생성된 JWT 토큰 문자열
     */
    private String createToken(String email, Role role, long expireTime) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * 토큰에서 Claims 추출
     * - 유효하지 않으면 예외 발생
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * JWT 토큰에서 이메일(email) 추출
     */
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }
    /**
     * JWT 토큰에서 사용자 권한(role) 추출
     */
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
