package com.exam.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberDTO memberDTO){
        if(memberService.checkMemberExist(memberDTO.getMember_id())){
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        if(!memberService.checkSignUpSamePassword(memberDTO)){
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        memberService.save(memberDTO);
        return ResponseEntity.ok("회원가입 성공");

    }

}
