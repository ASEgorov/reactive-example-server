package com.asegorov.reactive.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by Afh on 29.08.2016.
 */
public class UserInfo {
    private String name;
    private String action;
    private WebSocketSession session;

    @JsonIgnore
    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;

        UserInfo userInfo = (UserInfo) o;

        return session.equals(userInfo.session);

    }

    @Override
    public int hashCode() {
        return session.hashCode();
    }
}
