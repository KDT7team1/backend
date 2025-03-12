package com.exam.member;

import java.util.Optional;

public interface MemberService {

    // 회원가입
    MemberDTO registerMember(MemberDTO memberDTO);

    // 로그인
    MemberDTO loginMember(String memberId, String memberPasswd);

    // 아이디로 회원 정보 조회
    Optional<MemberDTO> getMemberById(String memberId);

}
