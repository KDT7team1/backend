package com.exam.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public int save(MemberDTO dto) {
        // 이미 존재하는 memberId인지 확인
        Optional<MemberEntity> existingMember = memberRepository.findByMemberId(dto.getMemberId());
        if (existingMember.isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        //String encodedPasswd = new BCryptPasswordEncoder().encode(dto.getMemberPasswd());
        dto.setMemberPasswd(dto.getMemberPasswd());  // 암호화된 비밀번호를 DTO에 설정

        // MemberDTO를 MemberEntity로 변환하여 저장
        MemberEntity member = MemberEntity.builder()
        					 .memberId(dto.getMemberId())
        					 .memberPasswd(dto.getMemberPasswd())
        					 .build();

        // JPA를 이용하여 DB에 저장
        memberRepository.save(member);

        return 1;  // 저장 성공 시 1 반환
    }

    @Override
    public MemberDTO findByMemberId(String memberId) {
        Optional<MemberEntity> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            return null; // 사용자 존재하지 않으면 null 반환
        }
        MemberDTO dto = new MemberDTO();
        dto.setMemberId(member.get().getMemberId().trim());
        dto.setMemberPasswd(member.get().getMemberPasswd().trim());
        return dto;
    }

//    @Override
//    public boolean login(String memberId, String memberPasswd) {
//        // 아이디 존재 여부 체크
//        MemberDTO member = findByMemberId(memberId);
//        if (member == null) {
//            System.out.println("아이디 없음");
//            return false;  // 아이디가 없으면 로그인 실패
//        }
//
//        System.out.println("입력된 비밀번호: " + memberPasswd);
//        System.out.println("저장된 암호화된 비밀번호: " + member.getMemberPasswd());
//        System.out.println("저장된 암호화된 비밀번호1: " + passwordEncoder.matches("1234", "$2a$10$fofN.QfVWmuWk0rDJXdig./vlaQi542DOIZKv1gnY8VOp4DWI5mtG"));
//        System.out.println("저장된 암호화된 비밀번호2: " + passwordEncoder.matches(memberPasswd, member.getMemberPasswd()));
//        /// //////////////////////////
//     
//
//        /// ///////////////////////
//        // 비밀번호 검증 (BCryptPasswordEncoder 사용)
//        if (passwordEncoder.matches(memberPasswd.trim(), member.getMemberPasswd().trim())) {
//            log.info("LOGGER>>>>>>>>>>>>>>>.matches: ");
//            return true;  // 비밀번호가 맞으면 로그인 성공
//        }
//
//        return false;  // 비밀번호 틀리면 로그인 실패
//    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // SecurityContextLogoutHandler로 로그아웃 처리
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

}
