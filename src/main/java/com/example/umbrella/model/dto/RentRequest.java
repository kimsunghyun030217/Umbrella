package com.example.umbrella.model.dto;

public class RentRequest {
    private String studentId;
    private int tableNumber;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public int getTableNumber(){
        return tableNumber;
    }

    public void setTableNumber(int tableNumber){
        this.tableNumber = tableNumber;
    }
}
