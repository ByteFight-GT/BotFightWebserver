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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class GameMatchResultHandler {

    private final GameMatchService gameMatchService;
    private final PlayerService playerService;
    private final SubmissionService submissionService;
    private final RabbitMQService rabbitMQService;
    private final EloCalculator eloCalculator;
    private final GameMatchLogService gameMatchLogService;


    public void handleGameMatchResult(GameMatchResult result) {
        long gameMatchId = result.matchId();
        if (!gameMatchService.isGameMatchIdExist(gameMatchId)) {
            log.warn("Game match id " + gameMatchId + " does not exist");
            return;
        }
        if (!gameMatchService.isGameMatchWaiting(gameMatchId)) {
            log.info("Game match is already played");
            return;
        }
        MATCH_STATUS status = result.status();
        GameMatchDTO gameMatchDTO = gameMatchService.getDTOById(gameMatchId);
        PlayerDTO player1DTO = gameMatchDTO.getPlayerOne();
        PlayerDTO player2DTO = gameMatchDTO.getPlayerTwo();

        log.info("Processing match result for game {}: {} vs {}, status: {}",
            gameMatchId, player1DTO.getName(), player2DTO.getName(), status);

        EloChanges eloChanges = new EloChanges();
        if (gameMatchDTO.getReason() == MATCH_REASON.LADDER) {
            eloChanges = eloCalculator.calculateElo(player1DTO, player2DTO, status);
            log.debug("Handling ladder match: player1 {}, player2 {}", player1DTO.getId(), player2DTO.getId());
            handleLadderResult(player1DTO, player2DTO, status, eloChanges);
            log.info("Ladder match handled");
        } else if (gameMatchDTO.getReason() == MATCH_REASON.VALIDATION) {
            SubmissionDTO submission1DTO = gameMatchDTO.getSubmissionOne();
            log.info("Processing validation match for player {}", player1DTO.getName());
            handleValidationResult(player1DTO, submission1DTO);
            log.info("Validation match handled");
        }
        gameMatchService.setGameMatchStatus(gameMatchId, status);
        gameMatchLogService.createGameMatchLog(gameMatchId, result.matchLog(), eloChanges.getPlayer1Change(), eloChanges.getPlayer2Change());
    }



    private void handleLadderResult(PlayerDTO player1DTO, PlayerDTO player2DTO, MATCH_STATUS status, EloChanges eloChanges) {
        // make this cleaner
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
        if (playerService.getCurrentSubmission(playerDTO.getId()).isEmpty()) {
            playerService.setCurrentSubmission(playerDTO.getId(), submissionDTO.id());
        }
    }

}
