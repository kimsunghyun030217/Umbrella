package com.example.umbrella.controller;

import com.example.umbrella.model.ResetCodeRequest;
import com.example.umbrella.model.ResetPasswordRequest;
import com.example.umbrella.model.User;
import com.example.umbrella.model.VerifyResetCodeRequest;
import com.example.umbrella.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // ✅ 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = authService.registerUser(user.getEmail(), user.getStudentId(), user.getPassword());
        return ResponseEntity.ok(newUser);
    }

    // ✅ 비밀번호 재설정 인증번호 요청 API - JSON 방식
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> sendResetCode(@RequestBody ResetCodeRequest request) {
        authService.sendResetCode(request.getEmail());
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    // ✅ 비밀번호 재설정 인증번호 확인 API - JSON 방식
    @PostMapping("/verify-reset-code")
    public ResponseEntity<String> verifyResetCode(@RequestBody VerifyResetCodeRequest request) {
        boolean isValid = authService.verifyResetCode(request.getEmail(), request.getCode());
        if (isValid) {
            return ResponseEntity.ok("인증번호 확인 성공!");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 올바르지 않습니다.");
        }
    }

    // ✅ 비밀번호 재설정 API - JSON 방식
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }
}
