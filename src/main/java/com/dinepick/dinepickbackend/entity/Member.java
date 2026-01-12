package com.dinepick.dinepickbackend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //탈퇴 여부
    @Column(nullable = false)
    private boolean deleted = false;

    //탈퇴 시각
    private LocalDateTime deletedAt;

    //계정상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Builder
    public Member(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.status = MemberStatus.ACTIVE;
        this.deleted = false;
    }

    //회원이름 변경
    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        this.name = name;
    }

    //회원 비밀번호 변경
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    //회원탈퇴
    public void withdraw() {
        if (this.deleted) {
            throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");
        }
        this.deleted = true;
        this.status = MemberStatus.WITHDRAWN;
        this.deletedAt = LocalDateTime.now();
    }

    //회원복구
    public void restore() {
        this.deleted = false;
        this.status = MemberStatus.ACTIVE;
        this.deletedAt = null;
    }
}
