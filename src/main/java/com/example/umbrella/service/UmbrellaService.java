package com.example.umbrella.service;

import com.example.umbrella.model.dto.RentRequest;
import com.example.umbrella.model.dto.ReturnRequest;
import com.example.umbrella.model.dto.UmbrellaStatusResponse;
import com.example.umbrella.model.entity.*;
import com.example.umbrella.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmbrellaService {

    private final UserRepository userRepository;
    private final UmbrellaRepository umbrellaRepository;
    private final RentRepository rentRepository;
    private final RentHistoryRepository rentHistoryRepository;

    public UmbrellaService(UserRepository userRepository,
                           UmbrellaRepository umbrellaRepository,
                           RentRepository rentRepository,
                           RentHistoryRepository rentHistoryRepository) {
        this.userRepository = userRepository;
        this.umbrellaRepository = umbrellaRepository;
        this.rentRepository = rentRepository;
        this.rentHistoryRepository = rentHistoryRepository;
    }

    @Transactional
    public String rentUmbrella(RentRequest request) {
        String studentId = request.getStudentId();
        int tableNumber = request.getTableNumber();
        String lockerId = request.getLockerId();

        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        if (user.getPenaltyBanUntil() != null && user.getPenaltyBanUntil().isAfter(LocalDateTime.now())) {
            return "연체로 인해 대여 제한 중입니다.";
        }

        if (rentRepository.findByUserAndReturnTimeIsNull(user).isPresent()) {
            return "이미 우산을 대여 중입니다.";
        }

        UmbrellaId umbrellaIdKey = new UmbrellaId(lockerId, tableNumber);
        Umbrella umbrella = umbrellaRepository.findById(umbrellaIdKey)
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

        user.setPenaltyDueDate(LocalDateTime.now().plusMinutes(1));
        userRepository.save(user);

        System.out.println("✅ 대여 처리됨: studentId=" + studentId + ", umbrellaId=" + umbrellaId);

        return "우산 대여가 완료되었습니다.";
    }

    @Transactional
    public String returnUmbrella(ReturnRequest request) {
        String studentId = request.getStudentId();
        int tableNumber = request.getTableNumber();
        String lockerId = request.getLockerId();

        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        Rent rent = rentRepository.findByUserAndReturnTimeIsNull(user)
                .orElseThrow(() -> new IllegalArgumentException("대여 중인 우산이 없습니다."));

        String rentedUmbrellaId = rent.getUmbrellaId();
        LocalDateTime now = LocalDateTime.now();
        rent.setReturnTime(now);

        if (user.getPenaltyDueDate() != null) {
            if (now.isAfter(user.getPenaltyDueDate())) {
                user.setPenaltyBanUntil(now.plusDays(3));
                System.out.println("❌ 연체 반납 → 대여 정지 처리됨 (until: " + user.getPenaltyBanUntil() + ")");
            } else {
                System.out.println("✅ 정시 반납 → 대여 정지 없음");
            }
            user.setPenaltyDueDate(null);
            userRepository.save(user);
        }

        UmbrellaId returnId = new UmbrellaId(lockerId, tableNumber);
        Umbrella returnSlot = umbrellaRepository.findById(returnId)
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

        System.out.println("✅ 반납 처리됨: studentId=" + studentId + ", umbrellaId=" + rentedUmbrellaId);

        return "우산 반납이 완료되었습니다.";
    }

    public List<Integer> getAvailableReturnSlots(String lockerId) {
        return umbrellaRepository.findById_LockerIdAndAvailableFalse(lockerId)
                .stream()
                .map(u -> u.getId().getTableNumber())
                .toList();
    }

    public List<Integer> getAvailableRentSlots(String lockerId) {
        List<Umbrella> availableSlots = umbrellaRepository.findById_LockerIdAndAvailableTrue(lockerId);

        return availableSlots.stream()
                .map(Umbrella::getTableNumber)
                .collect(Collectors.toList());
    }

    public List<UmbrellaStatusResponse> getAllUmbrellaStatus() {
        return umbrellaRepository.findAll().stream()
                .map(u -> new UmbrellaStatusResponse(
                        u.getUmbrellaId(),
                        u.getId().getTableNumber(),
                        u.isAvailable()))
                .toList();
    }

    public int getAvailableUmbrellaCount() {
        return umbrellaRepository.countByAvailableTrue();
    }

    public List<Integer> getAvailableUmbrellaSlots(String lockerId) {
        return umbrellaRepository.findById_LockerIdAndAvailableTrue(lockerId)
                .stream()
                .map(u -> u.getId().getTableNumber())
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

        // ✅ 연체 확인
        if (user.getPenaltyBanUntil() != null && user.getPenaltyBanUntil().isAfter(LocalDateTime.now())) {
            result.put("action", "banned");
            result.put("message", "연체로 인해 대여가 제한됩니다.");
            return ResponseEntity.ok(result);
        }

        // ✅ 대여 중 여부 확인
        Optional<Rent> activeRent = rentRepository.findByUserAndReturnTimeIsNull(user);

        System.out.println("🔍 studentId: " + studentId);
        System.out.println("🔍 user.id: " + user.getId());
        System.out.println("🔍 activeRent.isPresent: " + activeRent.isPresent());

        activeRent.ifPresent(r -> {
            System.out.println("🧾 rent_id: " + r.getId());
            System.out.println("🧾 umbrella_id: " + r.getUmbrellaId());
            System.out.println("🧾 rent_time: " + r.getRentTime());
            System.out.println("🧾 return_time: " + r.getReturnTime());
        });

        if (activeRent.isPresent()) {
            result.put("action", "return");
            result.put("message", "이미 대여 중입니다. 반납 후 다시 시도해주세요.");
            result.put("locker_id", lockerId);
            return ResponseEntity.ok(result);
        }

        // ✅ 대여 가능
        result.put("action", "rent");
        result.put("message", "대여 가능합니다.");
        result.put("locker_id", lockerId);
        return ResponseEntity.ok(result);
    }


    public Map<String, Object> getLockerStatus(String lockerId) {
        long count = umbrellaRepository.countById_LockerIdAndUmbrellaIdIsNotNull(lockerId);

        Map<String, Object> result = new HashMap<>();
        result.put("umbrellaCount", count);
        return result;
    }

    public List<Map<String, Object>> getAllLockerStatuses() {
        List<Umbrella> allUmbrellas = umbrellaRepository.findAll();

        Map<String, Long> lockerCountMap = allUmbrellas.stream()
                .filter(u -> u.getUmbrellaId() != null)
                .collect(Collectors.groupingBy(
                        u -> u.getId().getLockerId(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> response = new ArrayList<>();
        for (String lockerId : lockerCountMap.keySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("lockerId", lockerId);
            item.put("umbrellaCount", lockerCountMap.get(lockerId));
            response.add(item);
        }

        return response;
    }

    public Map<String, Object> getUmbrellaBoxStatus(String lockerId, int tableNumber) {
        UmbrellaId id = new UmbrellaId(lockerId, tableNumber);
        Optional<Umbrella> optional = umbrellaRepository.findById(id);

        if (optional.isEmpty()) return null;

        Umbrella umbrella = optional.get();

        Map<String, Object> result = new HashMap<>();
        result.put("umbrellaId", umbrella.getUmbrellaId());
        result.put("available", umbrella.isAvailable());
        result.put("updatedAt", umbrella.getUpdatedAt());

        return result;
    }
}
