package com.exam.member;

import java.util.Optional;

public interface MemberService {

    // 회원가입
    void registerMember(MemberDTO memberDTO);

    // 로그인
    MemberEntity login(String memberId, String memberPasswd);

//    // 로그아웃
//    void logoutMember(String token);

}
