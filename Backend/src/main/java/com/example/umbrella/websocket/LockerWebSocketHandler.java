package com.example.umbrella.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LockerWebSocketHandler extends TextWebSocketHandler {

    // lockerId → WebSocket 세션들
    private final ConcurrentHashMap<String, Set<WebSocketSession>> lockerSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery(); // "lockerId=A01"
        if (query == null || !query.startsWith("lockerId=")) return;

        String lockerId = query.split("=")[1];
        lockerSessions.computeIfAbsent(lockerId, key -> new CopyOnWriteArraySet<>()).add(session);

        System.out.println("✅ WebSocket 연결됨: " + lockerId + " (총 " + lockerSessions.get(lockerId).size() + "명)");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        lockerSessions.forEach((lockerId, sessions) -> sessions.remove(session));
    }

    // ✅ 실제로 메시지를 전송하는 메서드
    public void sendToLocker(String lockerId, String message) {
        Set<WebSocketSession> sessions = lockerSessions.get(lockerId);
        System.out.println("🧪 전송 시도: " + lockerId + " 세션 수 = " + (sessions != null ? sessions.size() : 0));

        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                        System.out.println("📤 전송 완료 → " + lockerId + ": " + message);
                    } catch (Exception e) {
                        System.err.println("❌ 전송 실패: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
