package com.example.umbrella.controller;

import com.example.umbrella.model.User;
import com.example.umbrella.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor  // Lombok이 생성자를 자동으로 생성합니다.
public class AuthController {
    private final AuthService authService;

    // ✅ 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = authService.registerUser(user.getEmail(), user.getStudentId(), user.getPassword());
        return ResponseEntity.ok(newUser);
    }

    // ✅ 비밀번호 재설정 인증번호 요청 API
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> sendResetCode(@RequestParam String email) {
        authService.sendResetCode(email); // 인증번호를 이메일로 발송
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    @PostMapping("/verify-reset-code") //비밀번호 재설정 인증번호 확인 API
    public ResponseEntity<String> verifyResetCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = authService.verifyResetCode(email, code);
        if (isValid) {
            return ResponseEntity.ok("인증번호 확인 성공!");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 올바르지 않습니다.");
        }
    }



    // ✅ 비밀번호 재설정 API
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }


}
