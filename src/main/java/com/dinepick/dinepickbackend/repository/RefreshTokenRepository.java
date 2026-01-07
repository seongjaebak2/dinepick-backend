package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByMemberId(Long memberId);

    void deleteByToken(String token);
}
