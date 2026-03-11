package com.example.umbrella.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketNfcResponse {
    private String action;   // "rent" or "return"
    private String message;  // 예: "대여 가능합니다"
}
