package com.example.umbrella.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 복합키 조인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "locker_id", referencedColumnName = "locker_id", nullable = false),
            @JoinColumn(name = "table_number", referencedColumnName = "table_number", nullable = false)
    })
    private Umbrella umbrella;

    @Column(name = "rent_time", nullable = false)
    private LocalDateTime rentTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "umbrella_id")
    private String umbrellaId;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Umbrella getUmbrella() { return umbrella; }
    public void setUmbrella(Umbrella umbrella) { this.umbrella = umbrella; }

    public LocalDateTime getRentTime() { return rentTime; }
    public void setRentTime(LocalDateTime rentTime) { this.rentTime = rentTime; }

    public LocalDateTime getReturnTime() { return returnTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getUmbrellaId() { return umbrellaId; }
    public void setUmbrellaId(String umbrellaId) { this.umbrellaId = umbrellaId; }
}
