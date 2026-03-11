package com.example.umbrella.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "umbrella")
public class Umbrella {

    @EmbeddedId
    private UmbrellaId id;

    @Column(name = "umbrella_id", length = 50)
    private String umbrellaId;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Umbrella() {}

    public Umbrella(UmbrellaId id, String umbrellaId, boolean available, LocalDateTime updatedAt) {
        this.id = id;
        this.umbrellaId = umbrellaId;
        this.available = available;
        this.updatedAt = updatedAt;
    }

    public UmbrellaId getId() { return id; }
    public void setId(UmbrellaId id) { this.id = id; }

    public String getUmbrellaId() { return umbrellaId; }
    public void setUmbrellaId(String umbrellaId) { this.umbrellaId = umbrellaId; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getTableNumber() {
        return id.getTableNumber();
    }

    public String getLockerId() {
        return id.getLockerId();
    }

}