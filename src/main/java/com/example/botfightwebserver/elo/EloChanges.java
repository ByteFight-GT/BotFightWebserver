package com.example.botfightwebserver.elo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EloChanges {
    private double player1Change = 0;
    private double player2Change = 0 ;
}
