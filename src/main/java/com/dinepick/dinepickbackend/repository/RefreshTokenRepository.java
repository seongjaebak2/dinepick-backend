package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("delete from RefreshToken r where r.member.id = :memberId")
    void deleteByMemberId(Long memberId);
}
