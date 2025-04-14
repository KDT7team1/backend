package com.exam.notification;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationRepository notificationRepository;

    @Override
    public SseEmitter createEmitter(String clientId) {
        SseEmitter emitter =  new SseEmitter(0L); // 무제한
        emitters.put(clientId, emitter);
        log.info("📡 SSE 연결 성공 : {}", clientId);

        // 연결 시, 누락된 알림 보내기
        List<Notification> missed = notificationRepository.findByClientIdAndIsSentFalse(clientId);
        for(Notification n:missed){
            try{
                emitter.send(SseEmitter.event().data(Map.of("type", n.getType(), "message", n.getMessage())));
                n.setIsSent(true); // 보낸뒤에 보냄으로 설정하기
            }catch (IOException e){
                log.warn("❌ 누락 알림 전송 실패: {}", n.getId());
            }
        }
        notificationRepository.saveAll(missed);


        emitter.onCompletion(() -> {
            emitters.remove(clientId);
            log.info("🚫 SSE 연결 종료: {}", clientId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(clientId);
            log.info("⌛ SSE 연결 타임아웃: {}", clientId);
        });

        emitter.onError((e) -> emitters.remove(clientId));
        return emitter;
    }

    // 알림 보내기
    @Override
    public void sendNotification(String clientId, String type, String message) {
        SseEmitter emitter = emitters.get(clientId);

        Notification notification = Notification.builder()
                .clientId(clientId)
                .type(type)
                .message(message)
                .isSent(false)
                .isRead(false)
                .build();

        try {
            if (emitter != null) {
                emitter.send(SseEmitter.event().data(Map.of("type", type, "message", message)));
                notification.setIsSent(true);
            }
        } catch (IOException e) {
            log.error("❌ SSE 전송 실패 (연결 끊김): {}", clientId);
            emitter.complete();
            emitters.remove(clientId);
        }

        notificationRepository.save(notification);
    }
}
