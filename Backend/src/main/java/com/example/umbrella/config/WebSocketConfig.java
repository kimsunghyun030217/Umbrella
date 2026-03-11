package com.example.umbrella.config;

import com.example.umbrella.websocket.LockerWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LockerWebSocketHandler lockerWebSocketHandler;

    public WebSocketConfig(LockerWebSocketHandler lockerWebSocketHandler) {
        this.lockerWebSocketHandler = lockerWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(lockerWebSocketHandler, "/ws/locker-updates")
                .setAllowedOrigins("*");
    }
}
