package com.example.botfightwebserver.rabbitMQ;

import com.example.botfightwebserver.gameMatch.GameMatch;
import com.example.botfightwebserver.gameMatch.GameMatchJob;
import com.example.botfightwebserver.gameMatch.GameMatchResult;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enqueueGameMatchJob(GameMatchJob job) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.GAME_MATCH_QUEUE, job);
    }

    public List<GameMatchJob> peekGameMatchQueue() {
        List<GameMatchJob> messages = new ArrayList<>();
        boolean hasMore = true;

        while (hasMore) {
            GameMatchJob message = (GameMatchJob) rabbitTemplate.receiveAndConvert(
                RabbitMQConfiguration.GAME_MATCH_QUEUE,
                1000
            );
            if (message == null) {
                hasMore = false;
            } else {
                messages.add(message);
                rabbitTemplate.convertAndSend(RabbitMQConfiguration.GAME_MATCH_QUEUE, message);
            }
        }
        return messages;
    }


    //only to be used for testing
    @VisibleForTesting
    public void enqueueGameMatchResult(GameMatchResult result) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.GAME_MATCH_RESULTS, result);
    }
}
