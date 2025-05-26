package com.example.umbrella.repository;

import com.example.umbrella.model.entity.RentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {
    // 특별한 쿼리는 필요 없고 기본 CRUD만 있으면 충분해
}
