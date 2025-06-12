// src/main/java/com/example/umbrella/model/entity/UmbrellaId.java

package com.example.umbrella.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UmbrellaId implements Serializable {

    @Column(name = "locker_id")
    private String lockerId;

    @Column(name = "table_number")
    private int tableNumber;

    public UmbrellaId() {}

    public UmbrellaId(String lockerId, int tableNumber) {
        this.lockerId = lockerId;
        this.tableNumber = tableNumber;
    }

    public String getLockerId() { return lockerId; }
    public void setLockerId(String lockerId) { this.lockerId = lockerId; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UmbrellaId)) return false;
        UmbrellaId that = (UmbrellaId) o;
        return tableNumber == that.tableNumber && Objects.equals(lockerId, that.lockerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lockerId, tableNumber);
    }
}
