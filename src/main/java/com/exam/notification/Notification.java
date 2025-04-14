package com.exam.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;
    private String type;
    private String message;

    private Boolean isSent;
    private Boolean isRead;

    private LocalDateTime createdAt;

    // notificationRepository.save(notification) 하기전에 JPA가 자동으로 아래 메서드를 호출
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (isSent == null) this.isSent = false;
        if (isRead == null) this.isRead = false;
    }


}
