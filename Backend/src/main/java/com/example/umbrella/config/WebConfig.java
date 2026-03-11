package com.example.umbrella.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOrigins("*")  // 프론트 주소만 지정해도 됨
                .allowedMethods("*")  // GET, POST, PUT 등 다 허용
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(false);  // 필요한 경우 true로
    }
}
