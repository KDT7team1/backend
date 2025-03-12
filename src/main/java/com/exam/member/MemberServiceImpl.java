package com.exam.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;
    final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    @Override
    public MemberDTO registerMember(MemberDTO memberDTO) {

        if (memberRepository.existsByMemberId(memberDTO.getMemberId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberDTO.getMemberPasswd());
        memberDTO.setMemberPasswd(encodedPassword);

        // 회원 정보를 MemberEntity로 변환하여 저장
        MemberEntity memberEntity = MemberEntity.builder()
                .memberId(memberDTO.getMemberId())
                .memberPasswd(memberDTO.getMemberPasswd())
                .memberUsername(memberDTO.getMemberUsername())
                .memberGender(memberDTO.getMemberGender())
                .memberNickname(memberDTO.getMemberNickname())
                .memberPhone(memberDTO.getMemberPhone())
                .memberBirthdate(memberDTO.getMemberBirthdate())
                .memberRole(memberDTO.getMemberRole())
                .memberAddress(memberDTO.getMemberAddress())
                .memberCreatedAt(memberDTO.getMemberCreatedAt())
                .build();

        MemberEntity savedMember = memberRepository.save(memberEntity);

        // 저장된 데이터를 DTO로 반환
        return new MemberDTO(
                savedMember.getMemberNo(),
                savedMember.getMemberId(),
                null,  // 비밀번호는 반환하지 않음
                savedMember.getMemberUsername(),
                savedMember.getMemberGender(),
                savedMember.getMemberNickname(),
                savedMember.getMemberPhone(),
                savedMember.getMemberBirthdate(),
                savedMember.getMemberRole(),
                savedMember.getMemberAddress(),
                savedMember.getMemberCreatedAt()
        );
    }

    // 로그인
    @Override
    public MemberDTO loginMember(String memberId, String password) {

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, memberEntity.getMemberPasswd())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // 로그인 성공 시 DTO로 반환 (비밀번호 제외)
        return new MemberDTO(
                memberEntity.getMemberNo(),
                memberEntity.getMemberId(),
                null,  // 비밀번호는 반환하지 않음
                memberEntity.getMemberUsername(),
                memberEntity.getMemberGender(),
                memberEntity.getMemberNickname(),
                memberEntity.getMemberPhone(),
                memberEntity.getMemberBirthdate(),
                memberEntity.getMemberRole(),
                memberEntity.getMemberAddress(),
                memberEntity.getMemberCreatedAt()
        );
    }

    // 회원 정보 조회
    @Override
    public Optional<MemberDTO> getMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(memberEntity -> new MemberDTO(
                        memberEntity.getMemberNo(),
                        memberEntity.getMemberId(),
                        null,  // 비밀번호는 반환하지 않음
                        memberEntity.getMemberUsername(),
                        memberEntity.getMemberGender(),
                        memberEntity.getMemberNickname(),
                        memberEntity.getMemberPhone(),
                        memberEntity.getMemberBirthdate(),
                        memberEntity.getMemberRole(),
                        memberEntity.getMemberAddress(),
                        memberEntity.getMemberCreatedAt()
                ));
    }
}