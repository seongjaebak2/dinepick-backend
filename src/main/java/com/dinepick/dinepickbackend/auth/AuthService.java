package com.dinepick.dinepickbackend.auth;

import com.dinepick.dinepickbackend.entity.Member;
import com.dinepick.dinepickbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public void signup(String email, String password) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이미 가입된 이메일");
        }

        Member member = new Member(
                email,
                passwordEncoder.encode(password)
        );
        memberRepository.save(member);
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        return jwtTokenProvider.createToken(
                member.getEmail(),
                member.getRole()
        );
    }
}

