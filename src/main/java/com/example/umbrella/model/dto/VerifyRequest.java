//DTO(Request용) : 이메일로 받은 인증번호를 검증할 때 사용하는 요청 데이터

package com.example.umbrella.model.dto;

public class VerifyRequest {
    private String email;
    private String code;

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
