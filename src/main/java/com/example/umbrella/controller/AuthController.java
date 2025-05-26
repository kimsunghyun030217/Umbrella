package com.example.umbrella.controller;

import com.example.umbrella.model.entity.User;
import com.example.umbrella.security.JwtUtil;
import com.example.umbrella.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 1. 헤더 유효성 검사
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Authorization header missing or invalid")
            );
        }

        String token = authHeader.substring(7); // "Bearer " 이후 토큰
        String id;
        try {
            id = jwtUtil.extractId(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "사용자 정보 없음")
            );
        }

        // 3. 사용자 조회
        Optional<User> userOpt = userService.findByStudentId(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "사용자 정보 없음")
            );
        }

        User user = userOpt.get();
        return ResponseEntity.ok(
                Map.of(
                        "id", user.getStudentId(),
                        "name", user.getName(),
                        "email", user.getEmail()
                )
        );
    }
}
