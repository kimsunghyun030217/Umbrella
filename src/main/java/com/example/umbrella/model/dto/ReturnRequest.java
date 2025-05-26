package com.example.umbrella.model.dto;

public class ReturnRequest {
    private String studentId;
    private int returnTableNumber;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getReturnTableNumber() {
        return returnTableNumber;
    }

    public void setReturnTableNumber(int returnTableNumber) {
        this.returnTableNumber = returnTableNumber;
    }
}
