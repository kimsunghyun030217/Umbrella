package com.example.umbrella.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id") // ✅ 핵심: user 비교를 위해 id 기준 equals/hashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "penalty_ban_until")
    private LocalDateTime penaltyBanUntil;

    @Column(name = "penalty_due_date")
    private LocalDateTime penaltyDueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // 필요 시 Rent와의 양방향 매핑도 추가 가능
    // @OneToMany(mappedBy = "user")
    // private List<Rent> rents;
}
