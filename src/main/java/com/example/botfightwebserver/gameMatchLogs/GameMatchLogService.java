package com.example.botfightwebserver.gameMatchLogs;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class GameMatchLogService {

    private final GameMatchLogRepository gameMatchLogRepository;

    public GameMatchLog createGameMatchLog(Long gameMatchId, String logs) {
        GameMatchLog gameMatchLog = new GameMatchLog();
        gameMatchLog.setMatchId(gameMatchId);
        gameMatchLog.setMatchLog(logs);
        return gameMatchLogRepository.save(gameMatchLog);
    }
}
