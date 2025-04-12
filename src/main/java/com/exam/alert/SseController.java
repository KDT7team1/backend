package com.exam.alert;


import com.exam.shop.ShopService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor// 해당 컨트롤러의 모든
public class SseController {

    private final SseService sseService;

    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam String clientId) {
        return sseService.createEmitter(clientId);
    }


}