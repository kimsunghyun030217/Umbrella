package com.example.umbrella.controller;

import com.example.umbrella.model.dto.LoginRequest;
import com.example.umbrella.model.dto.LoginResponse;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String studentId = request.getId();
        String password = request.getPassword();

        // 1. 사용자 존재 확인
        Optional<User> userOpt = userService.findByStudentId(studentId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(400).body(
                    Map.of("message", "아이디가 존재하지 않습니다.")
            );
        }

        User user = userOpt.get();

        // 2. 비밀번호 일치 확인
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "비밀번호가 틀립니다.")
            );
        }

        // 3. JWT 토큰 생성 (이메일과 이름 순서 수정)
        String token;
        try {
            // 🔽 여기서 로그 찍기
            System.out.println("studentId: " + user.getStudentId());
            System.out.println("name: " + user.getName());
            System.out.println("email: " + user.getEmail());

            token = jwtUtil.generateToken(user.getStudentId(), user.getName(), user.getEmail());
        } catch (Exception e) {
            e.printStackTrace(); // 🔽 예외 상세 출력 추가
            return ResponseEntity.status(500).body(
                    Map.of("message", "로그인 중 알 수 없는 오류가 발생했습니다.")
            );
        }
        // 4. 응답
        return ResponseEntity.ok(new LoginResponse("로그인 성공!", token));
    }
}
