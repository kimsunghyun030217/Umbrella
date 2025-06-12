package com.example.umbrella.controller;

import com.example.umbrella.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/fcm")
    public ResponseEntity<String> triggerFCMTest(@RequestParam String studentId) {
        testService.testSendFCMAfter1Minute(studentId);
        return ResponseEntity.ok("1분 뒤 FCM 알림 테스트 시작");
    }
}
