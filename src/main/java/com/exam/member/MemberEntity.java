package com.exam.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    Long memberNo;

    @Column(name = "member_id", nullable = false)
    String memberId;

    @Column(name = "member_passwd")
    String memberPasswd;

    @Column(name = "member_username")
    String memberUsername;

    @Column(name = "member_gender")
    String memberGender;

    @Column(name = "member_nickname")
    String memberNickname;

    @Column(name = "member_phone")
    String memberPhone;

    @Column(name = "member_birthdate")
    LocalDate memberBirthdate;

    @Column(name = "member_role")
    String memberRole;

    @Column(name = "member_address")
    String memberAddress;

    @Column(name = "member_created_at")
    LocalDateTime memberCreatedAt;

    // memberCreatedAt 필드가 null 이면 자동으로 현재 날짜와 시간 저장
    @PrePersist
    public void onCreate(){
        if (memberCreatedAt == null){
            memberCreatedAt = LocalDateTime.now();
        }
    }

}
