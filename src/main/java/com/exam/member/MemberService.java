package com.exam.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    // 회원가입
    int save(MemberDTO dto);

    // 회원 아이디로 사용자 찾기
    MemberDTO findByMemberId(String memberId);

    // 로그인 기능 추가
//    boolean login(String memberId, String memberPasswd);

    void logout(HttpServletRequest request, HttpServletResponse response);

}