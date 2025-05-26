// RentRepository: 사용자의 대여 중 여부를 확인하고, 특정 우산이 대여 중인지 확인할 수 있어.
package com.example.umbrella.repository;

import com.example.umbrella.model.entity.Rent;
import com.example.umbrella.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    Optional<Rent> findByUserAndReturnTimeIsNull(User user);
}
