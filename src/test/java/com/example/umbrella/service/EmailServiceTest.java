package com.example.umbrella.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private StringRedisTemplate redisTemplate;

    @InjectMocks
    private EmailService emailService;

    /**
     * 인증번호를 생성하고 이메일로 전송하는 기능 테스트
     */
    @Test
    void 인증코드_이메일전송_정상작동() {
        // given
        String email = "test@sch.ac.kr";
        ValueOperations<String, String> valueOps = Mockito.mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // when
        emailService.sendVerificationEmail(email);

        // then
        verify(redisTemplate.opsForValue())
                .set(startsWith("VERIFICATION:"), anyString(), eq(5L), eq(TimeUnit.MINUTES));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    /**
     * Redis에 저장된 인증번호와 사용자가 입력한 인증번호가 일치하면 true 반환
     */
    @Test
    void 인증코드_일치하면_검증성공() {
        // given
        String email = "test@sch.ac.kr";
        String inputCode = "123456";
        String redisKey = "VERIFICATION:" + email;

        ValueOperations<String, String> valueOps = Mockito.mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(redisKey)).thenReturn("123456");

        // when
        boolean result = emailService.verifyCode(email, inputCode);

        // then
        assertTrue(result);
    }

    /**
     * Redis에 저장된 인증번호와 입력이 다르면 false 반환
     */
    @Test
    void 인증코드_불일치하면_검증실패() {
        // given
        String email = "test@sch.ac.kr";
        String inputCode = "111111";
        String redisKey = "VERIFICATION:" + email;

        ValueOperations<String, String> valueOps = Mockito.mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(redisKey)).thenReturn("123456");

        // when
        boolean result = emailService.verifyCode(email, inputCode);

        // then
        assertFalse(result);
    }

    /**
     * 인증 완료 여부를 Redis에 저장하는 기능 테스트
     */
    @Test
    void 이메일_인증완료_저장성공() {
        // given
        String email = "test@sch.ac.kr";
        String redisKey = "VERIFIED:" + email;

        ValueOperations<String, String> valueOps = Mockito.mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // when
        emailService.markEmailVerified(email);

        // then
        verify(valueOps).set(redisKey, "true", 5L, TimeUnit.MINUTES);
    }

    /**
     * 비밀번호 재설정 인증번호 이메일 전송 테스트
     */
    @Test
    void 비밀번호재설정_이메일전송_정상작동() {
        // given
        String email = "test@sch.ac.kr";
        String code = "987654";

        // when
        emailService.sendResetCodeEmail(email, code);

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
