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

    public void sendLockerNotification(String lockerId, WebSocketNfcResponse response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            webSocketHandler.sendToLocker(lockerId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
