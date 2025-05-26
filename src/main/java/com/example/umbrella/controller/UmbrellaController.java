package com.example.umbrella.controller;

import com.example.umbrella.model.dto.*;
import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import com.example.umbrella.service.UmbrellaService;
import com.example.umbrella.security.JwtUtil;
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



    public UmbrellaController(UmbrellaService umbrellaService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.umbrellaService = umbrellaService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @PostMapping("/locker-status")
    public ResponseEntity<Map<String, Object>> checkLockerStatus(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LockerRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "Authorization header missing or invalid"));
        }

        String token = authHeader.substring(7);
        String studentId;

        try {
            studentId = jwtUtil.extractId(token);  // 토큰에서 studentId 추출
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid token"));
        }

        return umbrellaService.checkUserUmbrellaStatus(studentId, request.getLockerId());
    }



    @PostMapping("/rent")
    public ResponseEntity<String> rentUmbrella(@RequestBody RentRequest request) {
        String result = umbrellaService.rentUmbrella(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnUmbrella(@RequestBody ReturnRequest request) {
        String result = umbrellaService.returnUmbrella(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/return/slots")
    public ResponseEntity<List<Integer>> getReturnableSlots() {
        return ResponseEntity.ok(umbrellaService.getAvailableReturnSlots());
    }

    @GetMapping("/status")
    public ResponseEntity<?> getUmbrellaBoxStatus(@RequestParam("id") int tableNumber) {
        Map<String, Object> status = umbrellaService.getUmbrellaBoxStatus(tableNumber);
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
        Map<String, Object> status = umbrellaService.getLockerStatus(lockerId);
        return ResponseEntity.ok(status); // 항상 성공으로 간주하고 개수 0도 응답
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

        // ✅ 연체 판단
        if (user.getPenaltyDueDate() != null && user.getPenaltyDueDate().isBefore(now)) {
            // 연체 중
            return ResponseEntity.ok(Map.of(
                    "isOverdue", true,
                    "releaseDate", user.getPenaltyDueDate().toString()
            ));
        }

        // 연체 아님
        return ResponseEntity.ok(Map.of(
                "isOverdue", false
        ));
    }





}
