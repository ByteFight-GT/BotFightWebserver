package com.example.botfightwebserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
@RestController
public class BotFightWebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotFightWebServerApplication.class, args);
    }
}
