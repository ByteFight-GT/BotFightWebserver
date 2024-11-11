package com.example.botfightwebserver.player;

import com.example.botfightwebserver.submission.Submission;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PlayerTest{

    private static Validator validator;
    private Submission testSubmission;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setup() {
        testSubmission = new Submission();
    }

    @Test
    void testBuilderWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        // When
        Player player = Player.builder()
            .id(1L)
            .name("John Doe")
            .email("john@example.com")
            .creationDateTime(now)
            .lastModifiedDate(now)
            .elo(1500.0)
            .matchesPlayed(10)
            .numberWins(5)
            .numberLosses(3)
            .numberDraws(2)
            .currentSubmission(testSubmission)
            .build();

        assertEquals(1L, player.getId());
        assertEquals("John Doe", player.getName());
        assertEquals("john@example.com", player.getEmail());
        assertEquals(now, player.getCreationDateTime());
        assertEquals(now, player.getLastModifiedDate());
        assertEquals(1500.0, player.getElo());
        assertEquals(10, player.getMatchesPlayed());
        assertEquals(5, player.getNumberWins());
        assertEquals(3, player.getNumberLosses());
        assertEquals(2, player.getNumberDraws());
        assertEquals(testSubmission, player.getCurrentSubmission());
    }

    @Test
    void testDefaultValues() {
        Player player = Player.builder()
            .name("Jane Doe")
            .email("jane@example.com")
            .currentSubmission(testSubmission)
            .build();

        assertEquals(1200.0, player.getElo());
        assertEquals(0, player.getMatchesPlayed());
        assertEquals(0, player.getNumberWins());
        assertEquals(0, player.getNumberLosses());
        assertEquals(0, player.getNumberDraws());
    }

    @Test
    void testPrePersist() {
        Player player = Player.builder()
            .name("Test Player")
            .email("test@example.com")
            .currentSubmission(testSubmission)
            .build();

        player.onCreate();

        assertNotNull(player.getCreationDateTime());
        assertNotNull(player.getLastModifiedDate());
        assertTrue(player.getCreationDateTime().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(player.getLastModifiedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testSettersAndGetters() {
        Player player = new Player();

        player.setId(1L);
        player.setName("Test Player");
        player.setEmail("test@example.com");
        player.setElo(1400.0);
        player.setMatchesPlayed(5);
        player.setNumberWins(3);
        player.setNumberLosses(1);
        player.setNumberDraws(1);
        player.setCurrentSubmission(testSubmission);

        assertEquals(1L, player.getId());
        assertEquals("Test Player", player.getName());
        assertEquals("test@example.com", player.getEmail());
        assertEquals(1400.0, player.getElo());
        assertEquals(5, player.getMatchesPlayed());
        assertEquals(3, player.getNumberWins());
        assertEquals(1, player.getNumberLosses());
        assertEquals(1, player.getNumberDraws());
        assertEquals(testSubmission, player.getCurrentSubmission());
    }

    @Test
    void testCurrentSubmissionNull() {
        // Given
        Player player = Player.builder()
            .name("Test Player")
            .email("test@example.com")
            .build();
        Set<ConstraintViolation<Player>> violations = validator.validate(player);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        // Given
        Player player = Player.builder()
            .name("Test Player")
            .email("invalidEmail")
            .build();
        Set<ConstraintViolation<Player>> violations = validator.validate(player);
        System.out.println(violations);
        assertEquals(1, violations.size());
    }

}