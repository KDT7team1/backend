package com.exam.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.exam.member.MemberDTO;
import com.exam.member.MemberService;

import lombok.extern.slf4j.Slf4j;

//용도: 입력된 id와 pw 이용해서 DB와 연동해서 검증작업후 최종적으로  token 반환해주는 역할.

@Component
@Slf4j
public class JwtTokenProvider {

    MemberService memberService;
    JwtTokenService tokenService;

    public JwtTokenProvider(MemberService memberService, JwtTokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    public String authenticate(Map<String, String> map) {

        log.info("LOGGER: authenticate:{}", map);

        String encodedtoken=null;

        String memberId = map.get("memberId");
        String rawPasswd = map.get("memberPasswd");

        MemberDTO dto = memberService.findByMemberId(memberId);

        log.info("LOGGER: MemberDTO:{}", dto);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        log.info("LOGGER: matches:{}", passwordEncoder.matches(rawPasswd, dto.getMemberPasswd()));

        UsernamePasswordAuthenticationToken token = null;
        if( dto != null && passwordEncoder.matches(rawPasswd, dto.getMemberPasswd())) {

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new  SimpleGrantedAuthority("USER"));

            // 다음 token 정보가 세션에 저장된다.
//			token = new UsernamePasswordAuthenticationToken(memberPasswd, null, authorities);
            token = new UsernamePasswordAuthenticationToken(memberId, null, authorities);

            // Authentication 을 이용해서 token 생성
            encodedtoken = tokenService.generateToken(token);

        }//end if

        log.info("LOGGER: encodedtoken:{}", encodedtoken);
        return encodedtoken;
    }
}