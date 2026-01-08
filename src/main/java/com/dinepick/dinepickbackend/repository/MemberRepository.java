package com.dinepick.dinepickbackend.repository;

import com.dinepick.dinepickbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndDeletedFalse(String email);

    //탈퇴 회원 전체 조회
    List<Member> findAllByDeletedTrue();

    boolean existsByEmail(String email);
}
