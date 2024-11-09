package com.example.botfightwebserver.gameMatch;

import lombok.Data;

@Data
public class MatchSubmissionRequest {
    private Long player1Id;
    private Long player2Id;
    private Long submission1Id;
    private Long submission2Id;
    private MATCH_REASON reason;
}
