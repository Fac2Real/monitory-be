package com.factoreal.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 API", description = "웹소켓 알림 테스트용 API")
public class NotificationController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    // 웹소켓 테스트용 사이트
    // 1. 스프링 실행후 아래사이트에서 http://localhost:8080/websocket 으로 연결 (sockjs,stomp 체크)
    // 2. /topic/notify 구독
    // 3. swagger에서 아래 api 호출
    // https://jiangxy.github.io/websocket-debug-tool/
    @PostMapping("/notify")
    @Operation(summary = "알림 메시지 전송", description = "테스트용 알림 메시지를 WebSocket을 통해 전송합니다")
    public ResponseEntity<String> notify(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        simpMessagingTemplate.convertAndSend("/topic/notify", message);
        return ResponseEntity.ok("Message sent");
    }
}
