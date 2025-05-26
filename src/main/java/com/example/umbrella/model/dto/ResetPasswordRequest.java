//DTO : 비밀번호를 새로 설정할 때 필요한 요청 데이터 모델

package com.example.umbrella.model.dto;

public class ResetPasswordRequest {
    private String email;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}