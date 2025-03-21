package com.exam.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    // userid로 사용자 찾기
    Optional<Member> findByMemberId(String memberId);

}
