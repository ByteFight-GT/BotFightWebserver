package com.example.botfightwebserver.gameMatchLogs;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class GameMatchLogService {

    private final GameMatchLogRepository gameMatchLogRepository;

    public GameMatchLog createGameMatchLog(Long gameMatchId, String logs, double player1EloChange, double player2EloChange) {
        GameMatchLog gameMatchLog = new GameMatchLog();
        gameMatchLog.setMatchId(gameMatchId);
        gameMatchLog.setMatchLog(logs);
        gameMatchLog.setPlayer1EloChange(player1EloChange);
        gameMatchLog.setPlayer2EloChange(player2EloChange);
        return gameMatchLogRepository.save(gameMatchLog);
    }
}
