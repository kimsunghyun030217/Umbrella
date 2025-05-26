// RentalUserRepository.java
package com.example.umbrella.repository;

import com.example.umbrella.model.entity.User; // ✅ 반드시 필요함
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByStudentId(String studentId);
}
