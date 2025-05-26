//	이메일 발송 및 인증 코드 관리 (	인증 메일/재설정 코드 전송, Redis 저장 )

package com.example.umbrella.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender, StringRedisTemplate redisTemplate) {
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendVerificationEmail(String email) {
        String code = generateCode();
        redisTemplate.opsForValue().set("VERIFICATION:" + email, code, 5, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[우산 대여] 이메일 인증번호");
        message.setText("인증번호: " + code);
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    public void sendResetCodeEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[우산 대여] 비밀번호 재설정 인증번호");
        message.setText("인증번호: " + code);
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get("VERIFICATION:" + email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    public void markEmailVerified(String email) {
        redisTemplate.opsForValue().set("VERIFIED:" + email, "true", 5, TimeUnit.MINUTES);
    }
}
