package com.example.umbrella.service;

import com.example.umbrella.model.dto.RentRequest;
import com.example.umbrella.model.dto.ReturnRequest;
import com.example.umbrella.model.dto.UmbrellaStatusResponse;
import com.example.umbrella.model.entity.Rent;
import com.example.umbrella.model.entity.RentHistory;
import com.example.umbrella.model.entity.Umbrella;
import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmbrellaService {

    private final UserRepository userRepository;
    private final UmbrellaRepository umbrellaRepository;
    private final RentRepository rentRepository;
    private final HardwareService hardwareService;
    private final RentHistoryRepository rentHistoryRepository;

    public UmbrellaService(UserRepository userRepository,
                           UmbrellaRepository umbrellaRepository,
                           RentRepository rentRepository,
                           RentHistoryRepository rentHistoryRepository,
                           HardwareService hardwareService) {
        this.userRepository = userRepository;
        this.umbrellaRepository = umbrellaRepository;
        this.rentRepository = rentRepository;
        this.rentHistoryRepository = rentHistoryRepository;
        this.hardwareService = hardwareService;
    }

    public String rentUmbrella(RentRequest request) {
        String studentId = request.getStudentId();
        int tableNumber = request.getTableNumber();

        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        if (user.getPenaltyBanUntil() != null && user.getPenaltyBanUntil().isAfter(LocalDateTime.now())) {
            return "연체로 인해 대여 제한 중입니다.";
        }

        if (rentRepository.findByUserAndReturnTimeIsNull(user).isPresent()) {
            return "이미 우산을 대여 중입니다.";
        }

        Umbrella umbrella = umbrellaRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 우산함에 우산이 없습니다."));

        if (!umbrella.isAvailable()) {
            return "선택한 우산은 대여할 수 없습니다.";
        }

        String umbrellaId = umbrella.getUmbrellaId();

        umbrella.setUmbrellaId(null);
        umbrella.setAvailable(false);
        umbrella.setUpdatedAt(LocalDateTime.now());
        umbrellaRepository.save(umbrella);

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setUmbrella(umbrella);
        rent.setUmbrellaId(umbrellaId);
        rent.setRentTime(LocalDateTime.now());
        rent.setStudentId(studentId);
        rentRepository.save(rent);

        user.setPenaltyDueDate(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        hardwareService.unlock(tableNumber);

        return "우산 대여가 완료되었습니다.";
    }

    public String returnUmbrella(ReturnRequest request) {
        String studentId = request.getStudentId();
        int returnTableNumber = request.getReturnTableNumber();

        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        Rent rent = rentRepository.findByUserAndReturnTimeIsNull(user)
                .orElseThrow(() -> new IllegalArgumentException("대여 중인 우산이 없습니다."));

        String rentedUmbrellaId = rent.getUmbrellaId();
        LocalDateTime now = LocalDateTime.now();

        rent.setReturnTime(now);

        if (user.getPenaltyDueDate() != null && now.isAfter(user.getPenaltyDueDate())) {
            user.setPenaltyBanUntil(now.plusDays(3));
        }
        user.setPenaltyDueDate(null);
        userRepository.save(user);

        Umbrella returnSlot = umbrellaRepository.findByTableNumber(returnTableNumber)
                .orElseThrow(() -> new IllegalArgumentException("반납할 우산함 번호가 존재하지 않습니다."));

        if (returnSlot.getUmbrellaId() != null) {
            throw new IllegalStateException("선택한 반납 칸이 비어있지 않습니다.");
        }

        returnSlot.setUmbrellaId(rentedUmbrellaId);
        returnSlot.setAvailable(true);
        returnSlot.setUpdatedAt(now);
        umbrellaRepository.save(returnSlot);

        RentHistory history = new RentHistory();
        history.setStudentId(studentId);
        history.setUmbrellaId(rentedUmbrellaId);
        history.setRentTime(rent.getRentTime());
        history.setReturnTime(now);
        rentHistoryRepository.save(history);

        rentRepository.delete(rent);
        hardwareService.lock(returnTableNumber);

        return "우산 반납이 완료되었습니다.";
    }

    public List<Integer> getAvailableReturnSlots() {
        return umbrellaRepository.findByAvailableFalse().stream()
                .map(Umbrella::getTableNumber)
                .toList();
    }

    public List<UmbrellaStatusResponse> getAllUmbrellaStatus() {
        return umbrellaRepository.findAll().stream()
                .map(u -> new UmbrellaStatusResponse(
                        u.getUmbrellaId(),
                        u.getTableNumber(),
                        u.isAvailable()))
                .toList();
    }

    public int getAvailableUmbrellaCount() {
        return umbrellaRepository.countByAvailableTrue();
    }

    public List<Integer> getAvailableUmbrellaSlots() {
        return umbrellaRepository.findByAvailableTrue()
                .stream()
                .map(Umbrella::getTableNumber)
                .toList();
    }

    public ResponseEntity<Map<String, Object>> checkUserUmbrellaStatus(String studentId, String lockerId) {
        Map<String, Object> result = new HashMap<>();

        Optional<User> optionalUser = userRepository.findByStudentId(studentId);
        if (optionalUser.isEmpty()) {
            result.put("message", "해당 학번의 사용자가 없습니다.");
            return ResponseEntity.status(400).body(result);
        }

        User user = optionalUser.get();

        if (user.getPenaltyBanUntil() != null && user.getPenaltyBanUntil().isAfter(LocalDateTime.now())) {
            result.put("action", "banned");
            result.put("message", "연체로 인해 대여가 제한됩니다.");
            return ResponseEntity.ok(result);
        }

        Optional<Rent> activeRent = rentRepository.findByUserAndReturnTimeIsNull(user);
        if (activeRent.isPresent()) {
            result.put("action", "return");
            result.put("message", "이미 대여 중입니다. 반납 후 다시 시도해주세요.");
            result.put("roomId", lockerId); // 우산 반납할 우산함 정보 포함
            return ResponseEntity.ok(result);
        }

        result.put("action", "rent");
        result.put("message", "대여 가능합니다.");
        result.put("roomId", lockerId);
        return ResponseEntity.ok(result);
    }


    // ✅ 새로운 기능: 특정 우산함의 상태 조회
    public Map<String, Object> getUmbrellaBoxStatus(int tableNumber) {
        List<Umbrella> umbrellas = umbrellaRepository.findAllByTableNumber(tableNumber);


        if (umbrellas == null || umbrellas.isEmpty()) {
            return null;
        }

        int umbrellaCount = 0;
        int emptySlotCount = 0;

        for (Umbrella u : umbrellas) {
            if (u.getUmbrellaId() != null) umbrellaCount++;
            else emptySlotCount++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("umbrella", umbrellaCount);
        result.put("emptySlot", emptySlotCount);
        return result;
    }

    public List<Map<String, Object>> getAllLockerStatuses() {
        List<Umbrella> allUmbrellas = umbrellaRepository.findAll();

        // lockerId -> 개수로 그룹핑
        Map<String, Long> lockerCountMap = allUmbrellas.stream()
                .filter(u -> u.getUmbrellaId() != null) // 우산이 실제 존재하는 칸만 카운트
                .collect(Collectors.groupingBy(
                        Umbrella::getLockerId, // lockerId 기준 그룹
                        Collectors.counting()
                ));

        // Map -> JSON 응답형태로 변환
        List<Map<String, Object>> response = new ArrayList<>();
        for (String lockerId : lockerCountMap.keySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("lockerId", lockerId);
            item.put("umbrellaCount", lockerCountMap.get(lockerId));
            response.add(item);
        }

        return response;
    }

    public Map<String, Object> getLockerStatus(String lockerId) {
        long count = umbrellaRepository.countByLockerIdAndUmbrellaIdIsNotNull(lockerId); // 실제 우산이 있는 칸만 카운트

        Map<String, Object> result = new HashMap<>();
        result.put("umbrellaCount", count);
        return result;
    }


}
