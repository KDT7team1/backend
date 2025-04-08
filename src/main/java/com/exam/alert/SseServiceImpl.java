package com.exam.alert;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseServiceImpl implements SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter createEmitter(String clientId) {
        SseEmitter emitter =  new SseEmitter(30L * 1000 * 60); // 60분 유지

        emitters.put(clientId, emitter);
        log.info("📡 SSE 연결 성공: {}", clientId);


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
        if (emitter != null) {
            try {
                Map<String, String> payload = Map.of(
                        "type", type,
                        "message", message
                );
                emitter.send(SseEmitter.event()
                        .data(payload));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(clientId);
            }
        }
    }
}
