package com.example.umbrella.repository;

import com.example.umbrella.model.entity.Umbrella;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UmbrellaRepository extends JpaRepository<Umbrella, String> {

    long countByLockerIdAndUmbrellaIdIsNotNull(String lockerId);

    List<Umbrella> findByAvailableTrue();
    List<Umbrella> findByAvailableFalse();
    Optional<Umbrella> findByTableNumber(int tableNumber);
    Optional<Umbrella> findFirstByAvailableTrue();
    int countByAvailableTrue();

    // ✅ 여러 슬롯 가져오는 메서드 (우산함 상태 조회용)
    List<Umbrella> findAllByTableNumber(int tableNumber);
}
