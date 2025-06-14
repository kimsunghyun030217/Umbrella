package com.example.umbrella.service;

import com.example.umbrella.model.dto.WebSocketNfcResponse;
import com.example.umbrella.websocket.LockerWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final LockerWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public WebSocketService(LockerWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void sendLockerNotification(String lockerId, String actionOnly) {
        try {
            // 따옴표 없이 순수 문자열만 전송
            System.out.println("📦 텍스트 알림 전송: " + actionOnly);
            webSocketHandler.sendToLocker(lockerId, actionOnly);  // ← JSON 아님, 그냥 "rent"
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
