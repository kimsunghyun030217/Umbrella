package com.example.umbrella.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LockerWebSocketHandler extends TextWebSocketHandler {

    // ✅ 여러 세션을 저장
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

    // ✅ 클라이언트가 보낸 메시지를 같은 lockerId에 연결된 모든 세션에 브로드캐스트
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String query = session.getUri().getQuery(); // "lockerId=A01"
        if (query == null || !query.startsWith("lockerId=")) return;

        String lockerId = query.split("=")[1];
        System.out.println("📩 클라이언트로부터 메시지 수신: " + message.getPayload());

        // 받은 메시지를 같은 그룹의 모든 클라이언트에게 전송
        sendToLocker(lockerId, message.getPayload());
    }

    public void sendToLocker(String lockerId, String messageJson) {
        Set<WebSocketSession> sessions = lockerSessions.get(lockerId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(messageJson));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
