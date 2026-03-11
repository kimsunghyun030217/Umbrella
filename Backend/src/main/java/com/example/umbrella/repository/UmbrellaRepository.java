package com.example.umbrella.repository;

import com.example.umbrella.model.entity.Umbrella;
import com.example.umbrella.model.entity.UmbrellaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UmbrellaRepository extends JpaRepository<Umbrella, UmbrellaId> {

    List<Umbrella> findByAvailableTrue();

    List<Umbrella> findByAvailableFalse();



    int countByAvailableTrue();

    Optional<Umbrella> findFirstByAvailableTrue();

    Optional<Umbrella> findById_LockerIdAndId_TableNumber(String lockerId, int tableNumber);

    List<Umbrella> findById_LockerIdAndAvailableTrue(String lockerId);

    List<Umbrella> findById_LockerIdAndAvailableFalse(String lockerId);

    long countById_LockerIdAndUmbrellaIdIsNotNull(String lockerId);
}
