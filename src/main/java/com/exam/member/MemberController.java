package com.exam.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import com.exam.security.JwtTokenProvider;
import com.exam.security.JwtTokenResponse;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final MemberService memberService;
	JwtTokenProvider tokenProvider;
	
    public MemberController(MemberServiceImpl memberService, 
                            MemberRepository memberRepository, JwtTokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody MemberDTO dto) {
        logger.info("회원가입 요청: {}", dto);

        // 비밀번호 암호화 후 저장 (BCrypt 사용)
        String encodedPasswd = new BCryptPasswordEncoder().encode(dto.getMemberPasswd());
        dto.setMemberPasswd(encodedPasswd);

        // 회원 저장
        memberService.save(dto);
        return "회원가입 성공!";
    }

    // 로그인 처리 (세션 관리)
//    @PostMapping("/login")
//    public String login(@RequestBody MemberDTO dto) {
//        logger.info("LOGGER>>>>>>>>>>>>>>>.: {}", dto);
//        if (memberService.login(dto.getMemberId(), dto.getMemberPasswd())) {
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    dto.getMemberId(),
//                    null, // 비밀번호는 인증에만 사용
//                    new ArrayList<>()
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            logger.info("로그인 성공: {}", dto.getMemberId()); // 로그인 성공 로그 출력
//            return "로그인 성공!";
//        } else {
//            logger.info("로그인 실패: {}", dto.getMemberId()); // 로그인 실패 로그 출력
//            return "로그인 실패! 아이디 또는 비밀번호를 확인하세요.";
//        }
//    }
//    @PostMapping("/login")
//	public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody Map<String, String> map) {
//		
//		String token  = tokenProvider.authenticate(map);
//		
//		JwtTokenResponse response = 
//				new JwtTokenResponse(token, map.get("memberId"));
//		
//		return ResponseEntity.status(200).body(response);
//	}

    // 로그인한 사용자 정보 반환 API
    @GetMapping("/my")
    public ResponseEntity<Map<String, String>> getUserInfo(@RequestHeader("Authorization") String token) {
        String memberId = tokenProvider.getMemberIdFromToken(token.replace("Bearer ", ""));

        return ResponseEntity.ok(Map.of("memberId", memberId));
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // SecurityContextLogoutHandler를 사용하여 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // SecurityContext에서 인증 정보 삭제
        SecurityContextHolder.clearContext();

        return "로그아웃 성공!";
    }

    // 회원 정보 조회
    @GetMapping("/mypage/{memberId}")
    public MemberDTO mypage(@PathVariable String memberId) {
        MemberDTO member = memberService.findByMemberId(memberId);
        if (member == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return member;
    }

}
