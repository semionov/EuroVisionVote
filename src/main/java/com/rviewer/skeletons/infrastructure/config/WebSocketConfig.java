package com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.p2p.P2PWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final P2PWebSocketHandler p2PWebSocketHandler;

    public WebSocketConfig(P2PWebSocketHandler p2PWebSocketHandler) {
        this.p2PWebSocketHandler = p2PWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(p2PWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
