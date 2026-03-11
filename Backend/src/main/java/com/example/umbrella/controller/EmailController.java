package com.example.umbrella.controller;

import com.example.umbrella.model.dto.EmailRequest;
import com.example.umbrella.model.dto.VerifyRequest;
import com.example.umbrella.model.dto.ResetPasswordRequest;
import com.example.umbrella.security.JwtUtil;
import com.example.umbrella.service.EmailService;
import com.example.umbrella.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.umbrella.model.entity.User;


import java.util.Map;
import java.util.Optional;

@RestController
public class EmailController {

    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserService userService;

    public EmailController(EmailService emailService, UserService userService, JwtUtil jwtUtil) {
        this.emailService = emailService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    // 1. 이메일 중복 확인 (회원가입 시 사용)
    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (userService.isEmailExists(email)) {
            return ResponseEntity.badRequest().body("이미 가입한 이메일입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }


    // 2. 인증번호 발송 (회원가입 / 비밀번호 재설정)
    @PostMapping(value = "/send-email", consumes = "application/json")
    public ResponseEntity<?> sendVerificationCode(
            @RequestParam boolean isPasswordReset,
            @RequestBody EmailRequest request) {

        String email = request.getEmail();

        if (isPasswordReset) {
            if (!userService.isEmailExists(email)) {
                // 404 응답: 이메일 없음
                return ResponseEntity.status(404).body(
                        Map.of("message", "Email not found")
                );
            }
        } else {
            if (userService.isEmailExists(email)) {
                // 404 응답: 이미 가입된 이메일
                return ResponseEntity.status(404).body(
                        Map.of("message", "Email already exists")
                );
            }
        }

        // 인증번호 발송 (프론트에는 코드 미포함)
        emailService.sendVerificationEmail(email);

        // 200 OK 응답
        return ResponseEntity.ok(
                Map.of("message", "인증 메일 발송 성공")
        );
    }



    // 3. 인증번호 확인
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyRequest request) {
        boolean isValid = emailService.verifyCode(request.getEmail(), request.getCode());

        if (isValid) {
            emailService.markEmailVerified(request.getEmail());
            return ResponseEntity.ok(
                    Map.of("message", "인증 성공")
            );
        } else {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "인증번호가 올바르지 않습니다.")
            );
        }
    }

    @PostMapping("/changePw-verify-code")
    public ResponseEntity<?> issueTempToken(@RequestBody VerifyRequest request) {
        String email = request.getEmail();
        String code = request.getCode();

        // 이메일 존재 여부 확인
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "해당 이메일의 유저가 없습니다.")
            );
        }

        // 인증번호 검증
        boolean isValid = emailService.verifyCode(email, code);
        if (!isValid) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "인증번호가 올바르지 않습니다.")
            );
        }

        // 유저 정보로 임시 토큰 생성
        User user = userOpt.get();
        String tempToken = jwtUtil.generateTempToken(user.getStudentId(), email);

        return ResponseEntity.ok(Map.of("tempToken", tempToken));
    }

    @PostMapping("/changePw")
    public ResponseEntity<?> changePasswordWithToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ResetPasswordRequest request
    ) {
        // 1. Authorization 헤더 유효성 검사
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Authorization header missing or invalid")
            );
        }

        // 2. 토큰에서 이메일 추출
        String token = authHeader.substring(7); // "Bearer " 제거
        String emailFromToken;
        try {
            emailFromToken = jwtUtil.extractEmail(token); // JWT에서 이메일 꺼내는 메서드 필요
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "사용자 정보 없음")
            );
        }

        // 3. 이메일 일치 여부 확인
        if (!emailFromToken.equals(request.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "이메일이 토큰과 일치하지 않습니다.")
            );
        }

        // 4. 사용자 존재 여부 확인
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "해당 이메일의 유저가 없습니다.")
            );
        }

        // 5. 비밀번호 변경
        userService.updatePassword(request.getEmail(), request.getNewPassword());

        return ResponseEntity.ok().build(); // 200 OK, 바디 없음
    }
}
