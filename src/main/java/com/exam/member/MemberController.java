package com.exam.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public MemberDTO registerMember(@RequestBody MemberDTO memberDTO) {
        return memberService.registerMember(memberDTO);
    }

    // 로그인
    @PostMapping("/login")
    public MemberDTO loginMember(@RequestParam String memberId, @RequestParam String password) {
        return memberService.loginMember(memberId, password);
    }

    // 회원 정보 조회
    @GetMapping("/{memberId}")
    public MemberDTO getMemberById(@PathVariable String memberId) {
        return memberService.getMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
    }

}