//DTO : 비밀번호 재설정을 위해 인증번호를 요청할 때 사용하는 데이터 모델


package com.example.umbrella.model.dto;


public class ResetCodeRequest {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
