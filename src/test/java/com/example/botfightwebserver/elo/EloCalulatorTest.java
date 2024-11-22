package com.example.botfightwebserver.elo;

import com.example.botfightwebserver.gameMatch.MATCH_STATUS;
import com.example.botfightwebserver.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EloCalculatorTest {

    @Test
    void testCalculateEloPlayerOneWins() {
        EloCalculator eloCalculator = new EloCalculator();

        Player player1 = Player.builder()
                .name("Tyler Hansen")
                .email("tyler@example.com")
                .elo(1500.0)
                .build();

        Player player2 = Player.builder()
                .name("Patrick Kwok")
                .email("patrick@example.com")
                .elo(1500.0)
                .build();

        EloChanges eloChanges = eloCalculator.calculateElo(player1, player2, MATCH_STATUS.PLAYER_ONE_WIN);

        assertNotNull(eloChanges, "EloChanges should not be null");
        assertTrue(eloChanges.getPlayer1Change() > 0, "Player 1 Elo should increase");
        assertTrue(eloChanges.getPlayer2Change() < 0, "Player 2 Elo should decrease");
    }

    @Test
    void testCalculateEloPlayerTwoWins() {
        EloCalculator eloCalculator = new EloCalculator();

        Player player1 = Player.builder()
                .name("Tyler Hansen")
                .email("tyler@example.com")
                .elo(1500.0)
                .build();

        Player player2 = Player.builder()
                .name("Patrick Kwok")
                .email("patrick@example.com")
                .elo(1500.0)
                .build();

        EloChanges eloChanges = eloCalculator.calculateElo(player1, player2, MATCH_STATUS.PLAYER_TWO_WIN);

        assertNotNull(eloChanges, "EloChanges should not be null");
        assertTrue(eloChanges.getPlayer1Change() < 0, "Player 1 Elo should decrease");
        assertTrue(eloChanges.getPlayer2Change() > 0, "Player 2 Elo should increase");
    }

    @Test
    void testCalculateEloDraw() {
        EloCalculator eloCalculator = new EloCalculator();

        Player player1 = Player.builder()
                .name("Tyler Hansen")
                .email("tyler@example.com")
                .elo(1500.0)
                .build();

        Player player2 = Player.builder()
                .name("Patrick Kwok")
                .email("patrick@example.com")
                .elo(1500.0)
                .build();

        EloChanges eloChanges = eloCalculator.calculateElo(player1, player2, MATCH_STATUS.DRAW);

        assertNotNull(eloChanges, "EloChanges should not be null");
        assertTrue(eloChanges.getPlayer1Change() == 0, "Player 1 Elo should be the same");
        assertTrue(eloChanges.getPlayer2Change() == 0, "Player 2 Elo should be the same");
    }
}