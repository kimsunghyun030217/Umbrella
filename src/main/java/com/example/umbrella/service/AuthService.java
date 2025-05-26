//회원 인증 전체 처리(	회원가입, 인증코드 검증, 비밀번호 재설정)

// AuthService.java
package com.example.umbrella.service;

import com.example.umbrella.model.entity.User;
import com.example.umbrella.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       StringRedisTemplate redisTemplate,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }

    public User registerUser(String email, String studentId, String password) {
        String isVerified = redisTemplate.opsForValue().get("VERIFIED:" + email);
        if (!"true".equals(isVerified)) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        User user = new User();
        user.setEmail(email);
        user.setStudentId(studentId);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public void sendResetCode(String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다."));
        String code = String.valueOf((int)(Math.random() * 900000 + 100000));
        redisTemplate.opsForValue().set("RESET_CODE:" + email, code);
        redisTemplate.expire("RESET_CODE:" + email, java.time.Duration.ofMinutes(5));
        emailService.sendResetCodeEmail(email, code);
    }

    public boolean verifyResetCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get("RESET_CODE:" + email);
        return storedCode != null && storedCode.equals(code);
    }

    public void resetPassword(String email, String newPassword) {
        String isVerified = redisTemplate.opsForValue().get("VERIFIED:" + email);
        if (!"true".equals(isVerified)) {
            throw new IllegalStateException("이메일 인증이 필요합니다.");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
