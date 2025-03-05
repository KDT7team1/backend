package com.exam.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    @GetMapping("/signup")  // 회원가입
    public String signup(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession();
        Optional<Object> idOptional = Optional.ofNullable(session.getAttribute("member_id"));
        if(idOptional.isPresent()){
            return "redirect:/";
        }

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(MemberDTO memberDTO){
//        boolean isMemberExist = MemberService.checkMemberExist(memberDTO.getMember_id());
    return "";

    }


}
