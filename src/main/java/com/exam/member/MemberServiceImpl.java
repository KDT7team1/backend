package com.exam.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;
    final BCryptPasswordEncoder passwordEncoder;    // 비밀번호 암호화

    // 회원가입
    @Override
    public void registerMember(MemberDTO memberDTO) {
        // 회원 정보를 Entity로 변환하여 DB에 저장
        MemberEntity member = MemberEntity.builder()
                .memberId(memberDTO.getMemberId())
                .memberPasswd(passwordEncoder.encode(memberDTO.getMemberPasswd()))  // 암호화 된 비밀번호 저장
                .memberUsername(memberDTO.getMemberUsername())
                .memberGender(memberDTO.getMemberGender())
                .memberNickname(memberDTO.getMemberNickname())
                .memberPhone(memberDTO.getMemberPhone())
                .memberBirthdate(memberDTO.getMemberBirthdate())
                .memberRole(memberDTO.getMemberRole())
                .memberAddress(memberDTO.getMemberAddress())
                .memberCreatedAt(memberDTO.getMemberCreatedAt())
                .build();

        memberRepository.save(member);  // DB에 저장
    }

    // 로그인
    @Override
    public MemberEntity login(String memberId, String memberPasswd) {
        Optional<MemberEntity> member = memberRepository.findByMemberId(memberId);

        // 아이디가 존재하고 비밀번호가 일치하면 로그인 성공
        if (member.isPresent() && passwordEncoder.matches(memberPasswd, member.get().getMemberPasswd())) {
            return member.get();    // 로그인 성공
        }
        return null;    // 로그인 실패
    }

}