package com.example.botfightwebserver.gameMatch;

import java.io.Serializable;

public record GameMatchResult(Long matchId, MATCH_STATUS status, String matchLog) implements Serializable {
}
