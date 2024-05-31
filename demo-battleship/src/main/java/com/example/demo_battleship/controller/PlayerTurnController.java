package com.example.demo_battleship.controller;

import com.example.demo_battleship.model.PlayerTurn;
import com.example.demo_battleship.service.PlayerTurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/playerTurn")
public class PlayerTurnController {
    @Autowired
    private PlayerTurnService playerTurnService;

    @PostMapping("/add/{playerId}/{name}")
    public PlayerTurn addPlayerTurn(@PathVariable Integer playerId, @PathVariable String name) {
        return playerTurnService.addPlayerTurn(playerId, name);
    }

    @GetMapping("/{id}")
    public Optional<PlayerTurn> getPlayerTurnById(@PathVariable Integer id) {
        return playerTurnService.getPlayerTurnById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePlayerTurnById(@PathVariable Integer id) {
        playerTurnService.deletePlayerTurnById(id);
    }

}
