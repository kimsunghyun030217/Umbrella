package com.example.umbrella.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final long VERIFICATION_CODE_TTL = 5;

    // ✅ 인증번호 생성
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // ✅ 이메일로 인증번호 전송
    public void sendVerificationEmail(String email) {
        String code = generateVerificationCode();
        redisTemplate.opsForValue().set("VERIFICATION:" + email, code, VERIFICATION_CODE_TTL, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("우산 대여 시스템 이메일 인증번호");
        message.setText("인증번호: " + code + " (5분 이내 입력해주세요)");
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    public void sendResetCodeEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 재설정 인증번호");
        message.setText("인증번호: " + code + " (5분 이내 입력해주세요)");
        message.setFrom(fromEmail);
        mailSender.send(message);
    }

//엥

    // ✅ 인증번호 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get("VERIFICATION:" + email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // ✅ 이메일 인증 완료 처리
    public void markEmailVerified(String email) {
        redisTemplate.opsForValue().set("VERIFIED:" + email, "true", VERIFICATION_CODE_TTL, TimeUnit.MINUTES);
    }



}
