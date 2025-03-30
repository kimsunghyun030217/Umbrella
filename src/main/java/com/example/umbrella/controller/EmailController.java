package com.example.umbrella.controller;

import com.example.umbrella.service.EmailService;
import com.example.umbrella.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    // ✅ 이메일 중복 확인 API
    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        if (userService.isEmailExists(email)) {
            return ResponseEntity.badRequest().body("이미 가입한 이메일입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    // ✅ 이메일 인증번호 요청 API
    @PostMapping("/email-verification")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        if (userService.isEmailExists(email)) {
            return ResponseEntity.badRequest().body("이미 가입한 이메일입니다.");
        }

        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    // ✅ 인증번호 검증 API
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = emailService.verifyCode(email, code);

        if (isValid) {
            emailService.markEmailVerified(email);
            return ResponseEntity.ok("인증 성공!");
        } else {
            return ResponseEntity.status(400).body("인증 실패! 올바른 인증번호를 입력하세요.");
        }
    }
}
