package com.exam.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // userid로 사용자 찾기
    Optional<MemberEntity> findByMemberId(String memberId);

}
