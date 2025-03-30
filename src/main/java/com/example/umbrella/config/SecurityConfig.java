package com.example.umbrella.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ✅ BCryptPasswordEncoder 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Spring Security 설정 (인증 해제)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 🔹 CSRF 보호 비활성화 (POST 요청 차단 방지)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 🔹 모든 요청을 인증 없이 허용 (테스트용)
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // 🔹 H2 콘솔 사용 시 필요

        return http.build();
    }
}
