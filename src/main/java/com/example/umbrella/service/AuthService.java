package com.example.umbrella.service;

import com.example.umbrella.model.User;
import com.example.umbrella.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService; // ✅ 추가된 의존성

    // ✅ 회원가입
    public User registerUser(String email, String studentId, String password) {
        String isVerified = redisTemplate.opsForValue().get("VERIFIED:" + email);
        if (!"true".equals(isVerified)) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setStudentId(studentId);
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    // ✅ 비밀번호 재설정 인증번호 생성 및 이메일 발송
    public void sendResetCode(String email) {
        if (!userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다.");
        }

        String code = String.valueOf((int)(Math.random() * 900000 + 100000));
        redisTemplate.opsForValue().set("RESET_CODE:" + email, code);
        redisTemplate.expire("RESET_CODE:" + email, java.time.Duration.ofMinutes(5));

        // ✅ 여기서 code를 직접 넘겨야 해!
        emailService.sendResetCodeEmail(email, code);
    }


    public boolean verifyResetCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get("RESET_CODE:" + email);
        if (storedCode != null && storedCode.equals(inputCode)) {
            redisTemplate.opsForValue().set("VERIFIED:" + email, "true", java.time.Duration.ofMinutes(5));
            return true;
        }
        return false;
    }


    // ✅ 비밀번호 재설정
    public void resetPassword(String email, String newPassword) {
        String isVerified = redisTemplate.opsForValue().get("VERIFIED:" + email);
        if (!"true".equals(isVerified)) {
            throw new IllegalStateException("이메일 인증이 필요합니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
