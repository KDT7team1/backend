package com.exam.member;

public interface MemberService {

    // 관리자 아이디 존재여부 확인
    boolean checkMemberExist(String memberId);

    // 관리자 회원가입
    MemberDTO save(MemberDTO memberDTO);

    // 관리자 로그인
    MemberDTO login(String memberId, String passwd);

    // 비밀번호 일치 여부 확인 (비밀번호와 비밀번호 확인 필드가 일치하는지 체크)
    boolean checkSignUpSamePassword(MemberDTO memberDTO);

    // 관리자 비밀번호 변경
    boolean changePassword(String memberId, String oldPassword, String newPassword);

}
