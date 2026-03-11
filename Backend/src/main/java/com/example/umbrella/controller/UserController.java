package com.example.umbrella.controller;

import com.example.umbrella.model.dto.DeviceTokenUpdateRequest;
import com.example.umbrella.model.dto.SignUpRequest;
import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import com.example.umbrella.security.JwtUtil;
import com.example.umbrella.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    // ✅ 전체 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ 이메일로 사용자 조회
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));
    }

    // ✅ 디바이스 토큰 업데이트
    @PostMapping("/updateDeviceToken")
    public ResponseEntity<?> updateDeviceToken(@RequestBody DeviceTokenUpdateRequest request) {
        String token = request.getToken();
        String newDeviceToken = request.getDeviceToken();

        // ✅ 유효성 검사
        if (token == null || token.isBlank() || newDeviceToken == null || newDeviceToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "토큰 또는 디바이스 토큰 누락"));
        }

        // ✅ JWT에서 studentId 추출
        String studentId;
        try {
            studentId = jwtUtil.extractId(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 토큰입니다."));
        }

        // ✅ 사용자 조회
        Optional<User> userOpt = userService.findByStudentId(studentId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "사용자 정보 없음"));
        }

        User user = userOpt.get();

        // ✅ 디바이스 토큰이 다를 경우에만 갱신
        if (!newDeviceToken.equals(user.getDeviceToken())) {
            user.setDeviceToken(newDeviceToken);
            userService.save(user);  // 또는 userRepository.save(user);
        }

        return ResponseEntity.ok().build(); // 200 OK, 본문 없음
    }


    // ✅ 회원가입 API 추가
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody SignUpRequest request) {
        // 1. 이름 비어있는지 확인
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이름이 없습니다."));
        }

        // 2. 학번 형식 검증 (8자리 숫자)
        if (!request.getStudentId().matches("^\\d{8}$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "아이디는 8자리 숫자여야 합니다."));
        }

        // 3. 비밀번호 검증 (7자 이상, 특수문자 포함)
        if (!request.getPassword().matches("^(?=.*[@$!%*?&]).{7,}$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "비밀번호는 7자 이상이고 특수문자가 포함되어야 합니다."));
        }

        // 4. 디바이스 토큰 유무 확인
        if (request.getDeviceToken() == null || request.getDeviceToken().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "디바이스토큰이 없습니다."));
        }

        // 5. 아이디(학번) 중복 확인
        if (userRepository.findByStudentId(request.getStudentId()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "아이디가 이미 존재합니다."));
        }

        // 6. 저장
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setStudentId(request.getStudentId());
        user.setPassword(request.getPassword());
        user.setDeviceToken(request.getDeviceToken());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "회원가입 성공!"));
    }
}
