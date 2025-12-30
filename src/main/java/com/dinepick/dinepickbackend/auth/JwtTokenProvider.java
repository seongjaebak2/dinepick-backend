package com.dinepick.dinepickbackend.auth;

import com.dinepick.dinepickbackend.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY =
            "dinepick-secret-key-dinepick-secret-key";
    private final long EXPIRE_TIME = 1000 * 60 * 60;

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String createToken(String email, Role role) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
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

