package com.exam.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberDTO memberDTO) {
        memberService.registerMember(memberDTO);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO, HttpServletRequest request){
        MemberEntity member = memberService.login(memberDTO.getMemberId(), memberDTO.getMemberPasswd());

        if(member != null) {
            HttpSession session = request.getSession(); // 세션 생성
            session.setAttribute("loggedInUser", member);   // 세션에 사용자 정보 저장

            if ("ADMIN".equals(member.getMemberRole())) {
                return ResponseEntity.ok("관리자 페이지로 이동");
            } else {
                return ResponseEntity.ok("홈페이지로 이동");
            }
        } else {
            return ResponseEntity.status(401).body("로그인 실패! 아이디 또는 비밀번호를 확인하세요.");
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();   // 세션 삭제
        }
        return ResponseEntity.ok("로그아웃 성공!");
    }
}
