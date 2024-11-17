package com.example.botfightwebserver.gameMatch;

import com.example.botfightwebserver.player.Player;
import com.example.botfightwebserver.submission.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class GameMatchTest {
    private GameMatch gameMatch;
    private Player playerOne;
    private Player playerTwo;
    private Submission submissionOne;
    private Submission submissionTwo;
    private Clock fixedClock;

    private final LocalDateTime NOW = LocalDateTime.of(2024, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        playerOne = new Player();
        playerOne.setId(1L);

        playerTwo = new Player();
        playerTwo.setId(2L);

        submissionOne = new Submission();
        submissionOne.setId(1L);

        submissionTwo = new Submission();
        submissionTwo.setId(2L);

        gameMatch = new GameMatch();
        gameMatch.setPlayerOne(playerOne);
        gameMatch.setPlayerTwo(playerTwo);
        gameMatch.setSubmissionOne(submissionOne);
        gameMatch.setSubmissionTwo(submissionTwo);
        gameMatch.setMap("test_map");

        fixedClock = Clock.fixed(NOW.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        GameMatch.setClock(fixedClock);
    }

    @Test
    void testPrePersist() {
        gameMatch.onCreate();

        assertEquals(MATCH_STATUS.WAITING, gameMatch.getStatus());
        assertEquals(MATCH_REASON.UNKNOWN, gameMatch.getReason());
        assertEquals(NOW, gameMatch.getCreatedAt());
    }

    @Test
    void testCustomStatusAndReasonAreNotOverwritten() {
        gameMatch.setStatus(MATCH_STATUS.IN_PROGRESS);
        gameMatch.setReason(MATCH_REASON.LADDER);

        gameMatch.onCreate();

        assertEquals(MATCH_STATUS.IN_PROGRESS, gameMatch.getStatus());
        assertEquals(MATCH_REASON.LADDER, gameMatch.getReason());
    }


    @Test
    void testNoArgsConstructor() {
        GameMatch match = new GameMatch();

        assertNull(match.getId());
        assertNull(match.getPlayerOne());
        assertNull(match.getPlayerTwo());
        assertNull(match.getSubmissionOne());
        assertNull(match.getSubmissionTwo());
        assertNull(match.getStatus());
        assertNull(match.getReason());
        assertNull(match.getCreatedAt());
        assertNull(match.getProcessedAt());
        assertNull(match.getMap());
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now(fixedClock);
        LocalDateTime processed = LocalDateTime.now(fixedClock).plusMinutes(5);

        gameMatch.setId(1L);
        gameMatch.setStatus(MATCH_STATUS.IN_PROGRESS);
        gameMatch.setReason(MATCH_REASON.LADDER);
        gameMatch.setCreatedAt(now);
        gameMatch.setProcessedAt(processed);
        gameMatch.setMap("new_map");

        assertEquals(1L, gameMatch.getId());
        assertEquals(playerOne, gameMatch.getPlayerOne());
        assertEquals(playerTwo, gameMatch.getPlayerTwo());
        assertEquals(submissionOne, gameMatch.getSubmissionOne());
        assertEquals(submissionTwo, gameMatch.getSubmissionTwo());
        assertEquals(MATCH_STATUS.IN_PROGRESS, gameMatch.getStatus());
        assertEquals(MATCH_REASON.LADDER, gameMatch.getReason());
        assertEquals(now, gameMatch.getCreatedAt());
        assertEquals(processed, gameMatch.getProcessedAt());
        assertEquals("new_map", gameMatch.getMap());
    }
}