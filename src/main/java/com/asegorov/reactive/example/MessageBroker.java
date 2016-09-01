package com.asegorov.reactive.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Afh on 29.08.2016.
 */
@Service
public class MessageBroker {

    TopicProcessor<UserInfo> newUsers;
    TopicProcessor<UserInfo> deleteUsers;

    @Autowired
    ObjectMapper objectMapper;

    //Flux<List<UserInfo>> users;

    Set<UserInfo> users = new CopyOnWriteArraySet<>();

    public MessageBroker(){
        newUsers = TopicProcessor.create();
        deleteUsers = TopicProcessor.create();

        newUsers.subscribe(userInfo -> {
            users.add(userInfo);
        });

        deleteUsers.subscribe(userInfo -> {
            users.remove(userInfo);
        });

        // send user events to all users
        Flux.merge(newUsers, deleteUsers).subscribe(userInfo -> {
            Flux.fromIterable(users).parallel()
                    .filter(userInfo1 -> !userInfo.equals(userInfo1))
                    .subscribe(userInfo1 -> {
                        try {
                            userInfo1.getSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(userInfo)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        });

        // send users to new user
        newUsers.subscribe(userInfo -> {
            Flux.fromIterable(users)
                    .filter(userInfo1 -> !userInfo.equals(userInfo1))
                    .subscribe(userInfo1 -> {
                        try {
                            userInfo.getSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(userInfo1)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    public void newUser(UserInfo userInfo) {
        userInfo.setAction("Add");
        newUsers.onNext(userInfo);
    }

    public void deleteUser(UserInfo userInfo) {
        userInfo.setAction("Del");
        deleteUsers.onNext(userInfo);
    }
}
