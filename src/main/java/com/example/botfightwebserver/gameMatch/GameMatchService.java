package com.example.botfightwebserver.gameMatch;

import com.example.botfightwebserver.Elo.EloCalculator;
import com.example.botfightwebserver.Elo.EloChanges;
import com.example.botfightwebserver.gameMatchLogs.GameMatchLog;
import com.example.botfightwebserver.gameMatchLogs.GameMatchLogService;
import com.example.botfightwebserver.player.PlayerDTO;
import com.example.botfightwebserver.player.PlayerService;
import com.example.botfightwebserver.rabbitMQ.RabbitMQService;
import com.example.botfightwebserver.submission.Submission;
import com.example.botfightwebserver.submission.SubmissionDTO;
import com.example.botfightwebserver.submission.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameMatchService {

    private final GameMatchRepository gameMatchRepository;
    private final PlayerService playerService;
    private final SubmissionService submissionService;
    private final RabbitMQService rabbitMQService;
    private final EloCalculator eloCalculator;
    private final GameMatchLogService gameMatchLogService;

    public List<GameMatch> getGameMatches() {
        return gameMatchRepository.findAll();
    }

    public GameMatch createMatch(Long player1Id, Long player2Id, Long submission1Id, Long submission2Id, MATCH_REASON reason) {
        playerService.validatePlayers(player1Id, player2Id);
        submissionService.validateSubmissions(submission1Id, submission2Id);
        GameMatch gameMatch = new GameMatch();
        gameMatch.setPlayerOne(playerService.getPlayerReferenceById(player1Id));
        gameMatch.setPlayerTwo(playerService.getPlayerReferenceById(player2Id));
        gameMatch.setSubmissionOne(submissionService.getSubmissionReferenceById(submission1Id));
        gameMatch.setSubmissionTwo(submissionService.getSubmissionReferenceById(submission2Id));
        gameMatch.setStatus(MATCH_STATUS.WAITING);
        gameMatch.setReason(reason);
        return gameMatchRepository.save(gameMatch);
    }

    public GameMatchJob submitGameMatch(Long player1Id, Long player2Id, Long submission1Id, Long submission2Id, MATCH_REASON reason) {
        GameMatch match = createMatch(player1Id, player2Id, submission1Id, submission2Id, reason);
        GameMatchJob job = GameMatchJob.fromEntity(match);
        rabbitMQService.enqueueGameMatchJob(job);
        return job;
    }

    public void setGameMatchStatus(Long gameMatchId, MATCH_STATUS status) {
        GameMatch gameMatch = gameMatchRepository.findById(gameMatchId).get();
        gameMatch.setStatus(status);
        gameMatchRepository.save(gameMatch);
    }

    public GameMatchDTO getDTOById(Long id) {
        return GameMatchDTO.fromEntity(gameMatchRepository.getReferenceById(id));
    }

    public GameMatch getReferenceById(Long id) {
        return gameMatchRepository.getReferenceById(id);
    }

    }


