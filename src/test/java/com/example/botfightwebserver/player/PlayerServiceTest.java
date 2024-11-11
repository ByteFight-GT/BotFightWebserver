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

    private final LocalDateTime NOW = LocalDateTime.of(2006, 1, 2, 15, 30, 45);
    private final LocalDateTime LATER = LocalDateTime.of(2006, 2, 1, 15, 30, 45);

    @BeforeEach
    void setUp() {
        playerService = new PlayerService(playerRepository, eloCalculator, submissionService);
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
            .creationDateTime(NOW)
            .lastModifiedDate(LATER)
            .matchesPlayed(10)
            .numberWins(5)
            .numberLosses(3)
            .numberDraws(2)
            .build();

        Player player2 = Player.builder()
            .currentSubmission(submission2)
            .elo(1200.0)
            .email("bkwok123@gmail.com")
            .name("Ben")
            .creationDateTime(NOW)
            .lastModifiedDate(LATER)
            .matchesPlayed(10)
            .numberWins(5)
            .numberLosses(3)
            .numberDraws(2)
            .build();

        persistEntity(player1);
        persistEntity(player2);
        System.out.println(playerService.getPlayers());
    }
}