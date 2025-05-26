package com.example.umbrella.service;

import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service


public class PenaltyCheckService {

    private final UserRepository userRepository;
    private final FcmService fcmService;

    public PenaltyCheckService(UserRepository userRepository, FcmService fcmService) {
        this.userRepository = userRepository;
        this.fcmService = fcmService;
    }


    @Scheduled(cron = "0 0 9 * * *")
    public void sendPenaltyNotification() {
        LocalDateTime now = LocalDateTime.now();
        List<User> overdueUsers = userRepository.findByPenaltyDueDateBefore(now);

        for (User user : overdueUsers) {
            if (user.getDeviceToken() != null) {
                fcmService.sendNotification(
                        user.getDeviceToken(),
                        "우산 반납 기한 초과",
                        "반납 기한이 지났습니다. 지금 반납해주세요!"
                );
            }
        }
    }
}
