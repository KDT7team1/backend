package com.exam.member;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MemberDTO {

    Long member_no;
    String member_id;
    String member_passwd;
    String member_username;
    String member_gender;
    String member_nickname;
    String member_phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate member_birthdate;
    String member_role;
    String member_address;
    LocalDateTime member_created_at;

}
