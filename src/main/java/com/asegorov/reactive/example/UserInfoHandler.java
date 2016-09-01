package com.asegorov.reactive.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by Afh on 27.08.2016.
 */
@Component
public class UserInfoHandler extends TextWebSocketHandler {
    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection Established " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UserInfo userInfo = objectMapper.readValue(message.getPayload(), UserInfo.class);
        userInfo.setSession(session);
        messageBroker.newUser(userInfo);
        System.out.println("Connection closed " + message.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setSession(session);
        messageBroker.deleteUser(userInfo);
        System.out.println("Connection closed " + session.getId());
    }
}
