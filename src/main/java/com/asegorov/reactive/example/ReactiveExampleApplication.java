package com.asegorov.reactive.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableAutoConfiguration
public class ReactiveExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveExampleApplication.class, args);
	}
}
