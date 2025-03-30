package com.example.umbrella.service;

import com.example.umbrella.model.User;
import com.example.umbrella.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ 이메일 존재 여부 확인
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // ✅ 비밀번호 업데이트
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
