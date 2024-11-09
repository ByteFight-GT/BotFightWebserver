package com.example.botfightwebserver.player;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/players")
    public List<PlayerDTO> getPlayers() {
        return playerService.getPlayers();
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestParam String username,
                                               @RequestParam String email) {
        return ResponseEntity.ok(playerService.createPlayer(username, email));
    }

}
