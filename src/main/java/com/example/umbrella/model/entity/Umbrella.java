package com.example.umbrella.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "umbrella")
public class Umbrella {

    @Id
    @Column(name = "table_number")
    private int tableNumber; // ✅ PK로 바뀜

    @Column(name = "umbrella_id", length = 50)
    private String umbrellaId; // ✅ 일반 컬럼으로 변경됨

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "locker_id")
    private String lockerId;

    public String getLockerId() {
        return lockerId;
    }

    public void setLockerId(String lockerId) {
        this.lockerId = lockerId;
    }


    // Getters and Setters
    public String getUmbrellaId() { return umbrellaId; }
    public void setUmbrellaId(String umbrellaId) { this.umbrellaId = umbrellaId; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}