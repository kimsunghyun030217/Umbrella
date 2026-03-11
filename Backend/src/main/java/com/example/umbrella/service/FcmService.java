package com.example.umbrella.service;


import com.google.firebase.messaging.*;


import org.springframework.stereotype.Service;

@Service
public class FcmService {

    public void sendNotification(String deviceToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ 알림 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ 알림 전송 실패: " + e.getMessage());
        }
    }
}
