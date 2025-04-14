package com.exam.notification;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter createEmitter(String clientId);
    void sendNotification(String clientId, String type, String message);
}
