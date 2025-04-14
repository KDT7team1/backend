package com.exam.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // clientId로 isSent가 false인 컬럼을 조회
    List<Notification> findByClientIdAndIsSentFalse(String clientId);
}