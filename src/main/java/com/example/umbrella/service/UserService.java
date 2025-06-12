package com.example.umbrella.service;

import com.example.umbrella.model.dto.SignUpRequest;
import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional; // 이 import도 꼭 필요!




@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저가 없습니다."));
    }

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 이메일 중복 확인
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // ✅ 비밀번호 변경
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(newPassword); // ⚠️ 해시 없이 저장
        userRepository.save(user);
    }


    // ✅ 디바이스 토큰 업데이트
    public void updateDeviceToken(String studentId, String deviceToken) {
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setDeviceToken(deviceToken);
        userRepository.save(user);
    }

    public void registerUser(SignUpRequest request) {
        if (userRepository.findByStudentId(request.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 학번입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setStudentId(request.getStudentId());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ✅ 비번 암호화
        user.setDeviceToken(request.getDeviceToken());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }


}


