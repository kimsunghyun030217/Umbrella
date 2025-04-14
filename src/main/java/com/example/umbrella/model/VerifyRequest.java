package com.example.umbrella.model;

public class VerifyRequest {
    private String email;
    private String code;

    // 꼭 Getter & Setter 필요함!
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
