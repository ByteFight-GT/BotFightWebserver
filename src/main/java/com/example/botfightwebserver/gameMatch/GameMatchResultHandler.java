package com.example.botfightwebserver.gameMatch;

import com.example.botfightwebserver.elo.EloCalculator;
import com.example.botfightwebserver.elo.EloChanges;
import com.example.botfightwebserver.gameMatchLogs.GameMatchLogService;
import com.example.botfightwebserver.player.PlayerDTO;
import com.example.botfightwebserver.player.PlayerService;
import com.example.botfightwebserver.rabbitMQ.RabbitMQService;
import com.example.botfightwebserver.submission.SubmissionDTO;
import com.example.botfightwebserver.submission.SubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GameMatchResultHandler {

    private final GameMatchService gameMatchService;
    private final PlayerService playerService;
    private final SubmissionService submissionService;
    private final RabbitMQService rabbitMQService;
    private final EloCalculator eloCalculator;
    private final GameMatchLogService gameMatchLogService;


    public void handleGameMatchResult(GameMatchResult result) {
        long gameMatchId = result.matchId();
        MATCH_STATUS status = result.status();
        GameMatchDTO gameMatchDTO = gameMatchService.getDTOById(gameMatchId);
        PlayerDTO player1DTO = gameMatchDTO.getPlayerOne();
        PlayerDTO player2DTO = gameMatchDTO.getPlayerTwo();

        if (gameMatchDTO.getReason() == MATCH_REASON.LADDER) {
            handleLadderResult(player1DTO, player2DTO, status);
        } else if (gameMatchDTO.getReason() == MATCH_REASON.VALIDATION) {
            SubmissionDTO submission1DTO = gameMatchDTO.getSubmissionOne();
            handleValidationResult(player1DTO, submission1DTO);
        }
        gameMatchService.setGameMatchStatus(gameMatchId, status);
        gameMatchLogService.createGameMatchLog(gameMatchId, result.matchLog());
    }



    private void handleLadderResult(PlayerDTO player1DTO, PlayerDTO player2DTO, MATCH_STATUS status) {
        // make this cleaner
        EloChanges eloChanges = eloCalculator.calculateElo(player1DTO, player2DTO, status);
        if (status == MATCH_STATUS.PLAYER_ONE_WIN) {
            playerService.updatePlayerAfterLadderMatch(player1DTO, eloChanges.getPlayer1Change(), true, false);
            playerService.updatePlayerAfterLadderMatch(player2DTO, eloChanges.getPlayer2Change(), false, false);
        } else if (status == MATCH_STATUS.PLAYER_TWO_WIN) {
            playerService.updatePlayerAfterLadderMatch(player1DTO, eloChanges.getPlayer1Change(), false, false);
            playerService.updatePlayerAfterLadderMatch(player2DTO, eloChanges.getPlayer2Change(), true, false);
        } else if (status == MATCH_STATUS.DRAW) {
            playerService.updatePlayerAfterLadderMatch(player1DTO, eloChanges.getPlayer1Change(), false, true);
            playerService.updatePlayerAfterLadderMatch(player2DTO, eloChanges.getPlayer2Change(), false, true);
        }
    }

    private  void handleValidationResult(PlayerDTO playerDTO, SubmissionDTO submissionDTO) {
        submissionService.validateSubmissionAfterMatch(submissionDTO.id());
        playerService.setCurrentSubmissionIfNone(playerDTO.getId(), submissionDTO.id());
    }

}
