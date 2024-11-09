package com.example.botfightwebserver.gameMatchLogs;

import com.example.botfightwebserver.gameMatch.GameMatchService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class GameMatchLogService {

    private final GameMatchLogRepository gameMatchLogRepository;
    private final GameMatchService gameMatchService;

    public GameMatchLog createGameMatchLog(Long gameMatchId, String logs) {
        GameMatchLog gameMatchLog = new GameMatchLog();
        gameMatchLog.setGameMatch(gameMatchService.getReferenceById(gameMatchId));
        gameMatchLog.setMatchLog(logs);
        return gameMatchLogRepository.save(gameMatchLog);
    }
}
