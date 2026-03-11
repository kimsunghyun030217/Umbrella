package com.example.umbrella.model.dto;

public class UmbrellaStatusResponse {
    private String umbrellaId;
    private int tableNumber;
    private boolean available;

    public UmbrellaStatusResponse(String umbrellaId, int tableNumber, boolean available) {
        this.umbrellaId = umbrellaId;
        this.tableNumber = tableNumber;
        this.available = available;
    }

    public String getUmbrellaId() {
        return umbrellaId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isAvailable() {
        return available;
    }
}
