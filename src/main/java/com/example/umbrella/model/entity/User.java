package com.example.umbrella.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // ✅ 사용자 이름 추가

    @Column(nullable = false, unique = true)
    private String studentId;  // 학번 또는 고유 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @Column(name = "penalty_due_date")
    private LocalDateTime penaltyDueDate;

    @Column(name = "penalty_ban_until")
    private LocalDateTime penaltyBanUntil;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // === Getter & Setter ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public LocalDateTime getPenaltyDueDate() {
        return penaltyDueDate;
    }

    public void setPenaltyDueDate(LocalDateTime penaltyDueDate) {
        this.penaltyDueDate = penaltyDueDate;
    }

    public LocalDateTime getPenaltyBanUntil() {
        return penaltyBanUntil;
    }

    public void setPenaltyBanUntil(LocalDateTime penaltyBanUntil) {
        this.penaltyBanUntil = penaltyBanUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
