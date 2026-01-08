package com.dinepick.dinepickbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(length = 500)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDateTime expiredAt;

    public RefreshToken(String token, Member member, LocalDateTime expiredAt) {
        this.token = token;
        this.member = member;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}

