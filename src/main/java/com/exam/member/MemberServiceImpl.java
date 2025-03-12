package com.exam.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public boolean checkMemberExist(String memberId) {
        return false;
    }

    @Override
    public MemberDTO save(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public MemberDTO login(String memberId, String passwd) {
        return null;
    }

    @Override
    public boolean checkSignUpSamePassword(MemberDTO memberDTO) {
        return false;
    }

    @Override
    public boolean changePassword(String memberId, String oldPassword, String newPassword) {
        return false;
    }
}
