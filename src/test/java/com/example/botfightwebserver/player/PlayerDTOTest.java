package com.example.botfightwebserver.player;

import com.example.botfightwebserver.submission.Submission;
import com.example.botfightwebserver.submission.SubmissionDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PlayerDTOTest {

    @Test
    void fromEntity() {
        Submission submission = Submission.builder().id(1L).build();

//        Player player = Player.builder().id(1L).currentSubmission(submission).elo(1200).email("tkwok123@gmail.com")..build();
    }
}