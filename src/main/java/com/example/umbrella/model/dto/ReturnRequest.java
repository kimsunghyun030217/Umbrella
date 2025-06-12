package com.example.umbrella.model.dto;

public class ReturnRequest {
    private String studentId;
    private String lockerId;
    private int tableNumber; // ← 필드명 통일

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getLockerId() { return lockerId; }
    public void setLockerId(String lockerId) { this.lockerId = lockerId; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }
}
