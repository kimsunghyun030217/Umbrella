package com.example.umbrella.controller;

import com.example.umbrella.model.dto.*;
import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import com.example.umbrella.security.JwtUtil;
import com.example.umbrella.service.UmbrellaService;
import com.example.umbrella.service.WebSocketService;
import com.example.umbrella.model.dto.WebSocketNfcResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/umbrella")
public class UmbrellaController {

    private final UmbrellaService umbrellaService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final WebSocketService webSocketService; // ✅ 추가

    public UmbrellaController(
            UmbrellaService umbrellaService,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            WebSocketService webSocketService
    ) {
        this.umbrellaService = umbrellaService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.webSocketService = webSocketService;
    }

    // ✅ NFC 태깅 → 대여 가능 여부 + WebSocket 전송
    @PostMapping("/user_check_availability")
    public ResponseEntity<Map<String, Object>> checkLockerStatus(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LockerRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "Authorization header missing or invalid"));
        }

        String token = authHeader.substring(7);
        String studentId;
        try {
            studentId = jwtUtil.extractId(token);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid token"));
        }

        // 대여 가능 여부 판단
        ResponseEntity<Map<String, Object>> response = umbrellaService.checkUserUmbrellaStatus(studentId, request.getLockerId());

        // WebSocket 알림 전송
        boolean canRent = Boolean.TRUE.equals(response.getBody().get("canRent"));
        String mode = canRent ? "rent" : "return";
        String msg = canRent ? "대여 가능합니다" : "반납 가능합니다";

        webSocketService.sendLockerNotification(request.getLockerId(), new WebSocketNfcResponse(mode, msg));

        return response;
    }

    @PostMapping("/rent")
    public ResponseEntity<String> rentUmbrella(@RequestBody RentRequest request) {
        String result = umbrellaService.rentUmbrella(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnUmbrella(@RequestBody ReturnRequest request) {
        try {
            String result = umbrellaService.returnUmbrella(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/return/slots")
    public ResponseEntity<List<Integer>> getReturnableSlots(@RequestParam("lockerId") String lockerId) {
        return ResponseEntity.ok(umbrellaService.getAvailableReturnSlots(lockerId));
    }

    @GetMapping("/rent/slots")
    public ResponseEntity<List<Integer>> getRentableSlots(@RequestParam("lockerId") String lockerId) {
        return ResponseEntity.ok(umbrellaService.getAvailableRentSlots(lockerId));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getUmbrellaBoxStatus(
            @RequestParam("lockerId") String lockerId,
            @RequestParam("tableNumber") int tableNumber) {

        Map<String, Object> status = umbrellaService.getUmbrellaBoxStatus(lockerId, tableNumber);
        if (status == null) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 우산함을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/lockers/status")
    public ResponseEntity<?> getAllLockerStatuses() {
        return ResponseEntity.ok(umbrellaService.getAllLockerStatuses());
    }

    @GetMapping("/locker/{lockerId}/status")
    public ResponseEntity<?> getLockerStatus(@PathVariable String lockerId) {
        return ResponseEntity.ok(umbrellaService.getLockerStatus(lockerId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> checkOverdue(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("message", "인증 토큰이 없습니다."));
        }

        String token = authHeader.substring(7);
        String studentId;
        try {
            studentId = jwtUtil.extractId(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 토큰 형식입니다."));
        }

        Optional<User> optionalUser = userRepository.findByStudentId(studentId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "사용자 정보를 찾을 수 없습니다."));
        }

        User user = optionalUser.get();
        LocalDateTime now = LocalDateTime.now();

        boolean isOverdue = user.getPenaltyBanUntil() != null && user.getPenaltyBanUntil().isAfter(now);

        if (isOverdue) {
            return ResponseEntity.ok(Map.of(
                    "isOverdue", true,
                    "releaseDate", user.getPenaltyBanUntil().toString()  // ISO 포맷으로 반환
            ));
        } else {
            return ResponseEntity.ok(Map.of("isOverdue", false));
        }
    }


}
