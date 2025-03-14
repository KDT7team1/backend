package com.exam.member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MemberDTO {

    Long memberNo;          // 회원번호

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    String memberId;        // 회원 아이디

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    String memberPasswd;    // 비밀번호

    String memberUsername;  // 사용자 이름
    String memberGender;    // 성별
    String memberNickname;  // 닉네임
    String memberPhone;     // 전화번호
    LocalDate memberBirthdate;      // 생년월일
    String memberRole;      // 회원타입 (USER/ADMIN)
    String memberAddress;   // 주소
    LocalDateTime memberCreatedAt;  // 가입일

}
