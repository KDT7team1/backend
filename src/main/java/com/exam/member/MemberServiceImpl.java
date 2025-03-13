package com.exam.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository ;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository){

        this.memberRepository = memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean checkMemberExist(String member_id) {

//        Optional<Member> memberOptional = memberRepository.findById(member_id);
//        if(memberOptional.isPresent()){
//            log.error("동일한 아이디가 존재합니다.");
//            return true;
//        }else{
//            log.info("동일한 아이디가 없습니다.");
            return false;
//        }
    }

    @Override
    public void save(MemberDTO memberDTO) {

        String encodingPassword = passwordEncoder.encode(memberDTO.getMember_passwd());
        memberDTO.setMember_passwd(encodingPassword);
        log.info("로그인 한 ID {}", memberDTO.getMember_passwd());

        Member member = Member.builder()
                .member_no(memberDTO.getMember_no())
                .member_id(memberDTO.getMember_id())
                .member_passwd(encodingPassword)
                .member_username(memberDTO.getMember_username())
                .member_gender(memberDTO.getMember_gender())
                .member_nickname(memberDTO.getMember_nickname())
                .member_phone(memberDTO.getMember_phone())
                .member_birthdate(memberDTO.getMember_birthdate())
                .member_role(memberDTO.getMember_role())    // MemberRole.User
                .member_address(memberDTO.getMember_address())
                .member_created_at(memberDTO.getMember_created_at())
                .build();

        memberRepository.save(member);

    }

    @Override
    public boolean checkSignUpSamePassword(MemberDTO memberDTO) {

        if(!(memberDTO.getMember_passwd()).equals(memberDTO.getMember_passwd())) {
            log.error("비밀번호를 다시 입력해주세요.");
            return false;
        } else {
          log.info("비밀번호 일치");
          return true;
        }

    }

}
