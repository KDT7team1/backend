package com.exam.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor// 해당 컨트롤러의 모든
public class SseController {

    private final SseService sseService;
    private final NotificationRepository notificationRepository;

    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam String clientId) {
        return sseService.createEmitter(clientId);
    }


}