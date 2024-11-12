package com.example.botfightwebserver.player;

import com.example.botfightwebserver.PersistentTestBase;
import com.example.botfightwebserver.elo.EloCalculator;
import com.example.botfightwebserver.submission.Submission;
import com.example.botfightwebserver.submission.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class PlayerServiceTest extends PersistentTestBase {

    @Autowired
    private PlayerRepository playerRepository;

    @MockBean
    private SubmissionService submissionService;

    @MockBean
    private EloCalculator eloCalculator;

    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService(playerRepository, submissionService);
    }

    @Test
    void testGetPlayers() {
        Submission submission1 = persistAndReturnEntity(new Submission());
        Submission submission2 = persistAndReturnEntity(new Submission());

        Player player1 = Player.builder()
            .currentSubmission(submission1)
            .elo(1200.0)
            .email("tkwok123@gmail.com")
            .name("Tyler")
            .matchesPlayed(10)
            .numberWins(5)
            .numberLosses(3)
            .numberDraws(2)
            .build();

        Player player2 = Player.builder()
            .currentSubmission(submission2)
            .elo(1400.0)
            .email("bkwok123@gmail.com")
            .name("Ben")
            .matchesPlayed(5)
            .numberWins(3)
            .numberLosses(2)
            .numberDraws(0)
            .build();

        persistEntity(player1);
        persistEntity(player2);
        List<PlayerDTO> players = playerService.getPlayers();
        PlayerDTO player1DTO = players.get(0);
        PlayerDTO player2DTO = players.get(1);

        assertEquals(submission1.getId(), player1DTO.getCurrentSubmissionDTO().id());
        assertEquals(1200.0, player1DTO.getElo());
        assertEquals("Tyler", player1DTO.getName());
        assertEquals("tkwok123@gmail.com", player1DTO.getEmail());
        assertEquals(10, player1DTO.getMatchesPlayed());
        assertEquals(3, player1DTO.getNumberLosses());
        assertEquals(2, player1DTO.getNumberDraws());
        assertEquals(5, player1DTO.getNumberWins());

        assertEquals(submission2.getId(), player2DTO.getCurrentSubmissionDTO().id());
        assertEquals(1400.0, player2DTO.getElo());
        assertEquals("Ben", player2DTO.getName());
        assertEquals("bkwok123@gmail.com", player2DTO.getEmail());
        assertEquals(5, player2DTO.getMatchesPlayed());
        assertEquals(3, player2DTO.getNumberWins());
        assertEquals(2, player2DTO.getNumberLosses());
        assertEquals(0, player2DTO.getNumberDraws());
    }
}