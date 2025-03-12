package com.exam.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long member_no;

    @Column(name = "member_id", nullable = false, length = 20)
    String member_id;

    @Column(name = "member_passwd", nullable = false, length = 100) // 암호화 대비
    String member_passwd;

    @Column(name = "member_username", nullable = false, length = 20)
    String member_username;

    @Column(nullable = false, length = 10)
    String member_gender;

    @Column(nullable = false, length = 30)
    String member_nickname;

    @Column(nullable = false, length = 15)
    String member_phone;

    LocalDate member_birthdate;

    @Column(nullable = false, length = 20)
    String member_role;

    @Column(nullable = false, length = 100)
    String member_address;

    @Column(updatable = false)  // 생성 날짜는 변경 불가
    LocalDateTime member_created_at;

}
