package com.example.botfightwebserver.rabbitMQ;

import com.example.botfightwebserver.gameMatch.GameMatchJob;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rabbit")
@RequiredArgsConstructor
public class RabbitMQController {

    private final RabbitMQService rabbitMQService;

}
