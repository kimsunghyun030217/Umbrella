package com.example.umbrella.model.dto;

public class LockerResponse {

    private String action;
    private String roomId;

    public LockerResponse(String action, String roomId) {
        this.action = action;
        this.roomId = roomId;
    }

    public String getAction() { return action; }
    public String getRoomId() { return roomId; }

}
