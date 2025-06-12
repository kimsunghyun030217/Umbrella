package com.example.umbrella.service;

import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserRepository userRepository;
    private final FcmService fcmService;

    public void testSendFCMAfter1Minute(String studentId) {
        Optional<User> optionalUser = userRepository.findByStudentId(studentId);
        if (optionalUser.isEmpty() || optionalUser.get().getDeviceToken() == null) {
            System.out.println("유저가 없거나 디바이스 토큰이 없습니다.");
            return;
        }

        User user = optionalUser.get();


        System.out.println("1분 뒤에 FCM 발송 예약됨");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fcmService.sendNotification(
                        user.getDeviceToken(),
                        "테스트 알림",
                        "1분이 지나 알림을 전송합니다!"
                );
                System.out.println("1분 뒤 FCM 전송 완료!");
            }
        }, 60 * 1000); // 1분 뒤 실행
    }
}
