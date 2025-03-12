package com.exam.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MemberDTO {

    Long member_no;  // 회원 번호 (가입 요청 시 필요 X)

    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
    String member_id;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    String member_passwd;

    @NotBlank(message = "비밀번호 확인")
    String member_passwd_confirm;

    @NotBlank(message = "이름을 입력하세요.")
    String member_username;

    @NotBlank(message = "핸드폰 번호를 입력하세요.")
    @Pattern(regexp = "010-\\d{4}-\\d{4}", message = "올바른 핸드폰 번호 형식(010-xxxx-xxxx)이어야 합니다.")
    String member_phone;

    @Builder.Default
    String member_role = "ADMIN";   // 관리자 계정 고정

    LocalDateTime member_created_at;

}
