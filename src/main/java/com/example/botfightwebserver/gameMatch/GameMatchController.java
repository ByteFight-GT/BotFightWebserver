package com.example.botfightwebserver.gameMatch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/game-match")
public class GameMatchController {

    private final GameMatchService gameMatchService;

    @PostMapping("/submit")
    public ResponseEntity<GameMatchJob> submitMatch(@RequestBody MatchSubmissionRequest request) {
        GameMatchJob job = gameMatchService.submitGameMatch(
            request.getPlayer1Id(),
            request.getPlayer2Id(),
            request.getSubmission1Id(),
            request.getSubmission2Id(),
            request.getReason()
        );
        return ResponseEntity.ok(job);
    }

}
