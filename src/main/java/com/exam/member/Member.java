package com.exam.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Member {

    @Id
    @GeneratedValue
    Long member_no;

    @Column(name = "member_id", nullable = false, length = 20)
    String member_id;

    @Column(name = "member_passwd", nullable = false, length = 20)
    String member_passwd;

    @Column(name = "member_username", nullable = false, length = 20)
    String member_username;

    String member_gender;
    String member_nickname;
    String member_phone;
    LocalDate member_birthdate;
    String member_role;
    String member_address;
    LocalDateTime member_created_at;

}
