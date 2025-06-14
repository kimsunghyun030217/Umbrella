package com.example.umbrella.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.umbrella.model.entity.RentHistory;

@Entity
@Table(name = "rent_history")
public class RentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String umbrellaId;

    @Column(name = "rent_time")
    private LocalDateTime rentTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUmbrellaId() {
        return umbrellaId;
    }

    public void setUmbrellaId(String umbrellaId) {
        this.umbrellaId = umbrellaId;
    }

    public LocalDateTime getRentTime() {
        return rentTime;
    }

    public void setRentTime(LocalDateTime rentTime) {
        this.rentTime = rentTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }


}
