package com.asegorov.reactive.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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

    ConcurrentHashMap<UserInfo, UserInfo> users = new ConcurrentHashMap<>();

    public MessageBroker(){
        newUsers = TopicProcessor.create();
        deleteUsers = TopicProcessor.create();

        Flux<UserInfo> newUsersFiltered = newUsers.filter(userInfo -> !users.contains(userInfo));

        newUsersFiltered.subscribe(userInfo -> users.put(userInfo, userInfo));

        Flux<UserInfo> deleteUsersWithNames = deleteUsers.filter(users::contains).map(userInfo -> users.get(userInfo));
        deleteUsersWithNames.subscribe(userInfo -> {
            users.remove(userInfo);
        });

        // send user events to all users
        Flux.merge(newUsersFiltered, deleteUsersWithNames).subscribe(userInfo -> {
            HashMap<UserInfo, UserInfo> usr = new HashMap(users);
            Flux.fromIterable(usr.values()).parallel()
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
            Flux.fromIterable(users.values())
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
