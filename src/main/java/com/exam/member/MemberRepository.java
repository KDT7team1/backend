package com.exam.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 아이디로 관리자 조회
    Optional<MemberEntity> findByMemberId(String memberId);

    // 관리자 아이디 존재 여부 확인
    boolean existsByMemberId(String memberId);

}
