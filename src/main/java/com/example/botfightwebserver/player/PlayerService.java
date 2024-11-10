package com.example.botfightwebserver.player;

import com.example.botfightwebserver.elo.EloCalculator;
import com.example.botfightwebserver.submission.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final EloCalculator eloCalculator;
    private final SubmissionService submissionService;

    public List<PlayerDTO> getPlayers() {
        return playerRepository.findAll()
            .stream()
            .map(PlayerDTO::fromEntity)
            .collect(Collectors.toUnmodifiableList());
    }

    public Player getPlayerReferenceById(Long id) {
        return playerRepository.getReferenceById(id);
    }

    public PlayerDTO getDTOById(Long id) {
        return PlayerDTO.fromEntity(playerRepository.getReferenceById(id));
    }


    public PlayerDTO createPlayer(String name, String email) {
        if (playerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Player with email " + email + " already exists");
        }
        Player player = new Player();
        player.setName(name);
        player.setEmail(email);
        return PlayerDTO.fromEntity(playerRepository.save(player));
    }

    public void validatePlayers(Long player1Id, Long player2Id) {
        if (!playerRepository.existsById(player1Id) || !playerRepository.existsById(player2Id)) {
            throw new IllegalArgumentException("One or both players do not exist");
        }
        if (player1Id.equals(player2Id)) {
            throw new IllegalArgumentException("Players must be different");
        }
    }

    public PlayerDTO updatePlayerAfterLadderMatch(PlayerDTO playerDTO, double eloChange, boolean isWin, boolean isDraw) {
        if(isWin && isDraw) {
            throw new IllegalArgumentException("Result can't be a win and a draw");
        }
        double currentElo = playerDTO.getElo();
        double newElo = currentElo + eloChange;
        Player player = playerRepository.findById(playerDTO.getId()).get();
        player.setElo(newElo);
        player.setMatchesPlayed(player.getMatchesPlayed() + 1);
        if (!isWin && !isDraw) {
            player.setNumberLosses(player.getNumberLosses() + 1);
        } else if (isWin) {
            player.setNumberWins(player.getNumberWins() + 1);
        } else if (isDraw) {
            player.setNumberDraws(player.getNumberDraws() + 1);
        }
        return PlayerDTO.fromEntity(playerRepository.save(player));
    }

    public boolean setCurrentSubmissionIfNone(Long playerId, Long submissionId) {
        Player player = playerRepository.findById(playerId).get();
        if (player.getCurrentSubmission() == null) {
            player.setCurrentSubmission(submissionService.getSubmissionReferenceById(submissionId));
            return true;
        }
        return false;
    }
}


