package com.dinepick.dinepickbackend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 🔥 회원가입용 생성자
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.name = "USER"; // 또는 프론트에서 받게 바꿔도 됨
        this.role = Role.ROLE_USER;
    }

    public Member(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

}
