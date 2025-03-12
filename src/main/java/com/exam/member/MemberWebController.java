package com.exam.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberWebController {

    @GetMapping("/signup")  // 회원가입
    public String signup(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("member_id") != null){
            return "redirect:/member";
        }

        return "signup";
    }

}
