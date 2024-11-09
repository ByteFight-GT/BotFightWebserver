package com.example.botfightwebserver.Elo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EloChanges {
    private final double player1Change;
    private final double player2Change;
}
