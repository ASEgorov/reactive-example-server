package com.asegorov.reactive.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Created by Afh on 27.08.2016.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    UserInfoHandler userInfoHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        //registry.addHandler(userInfoHandler, "ws/userinfo").setAllowedOrigins("*").setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()));
        registry.addHandler(userInfoHandler, "ws/userinfo").setAllowedOrigins("*");//.setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()));

    }
}
