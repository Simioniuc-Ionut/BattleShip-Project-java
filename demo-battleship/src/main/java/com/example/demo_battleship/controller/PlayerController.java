package com.example.demo_battleship.controller;

import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/list")
    public List<Player> listPlayers() {
        return playerService.listPlayers();
    }

    @PostMapping("/add")
    public Player addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player);
    }

    @GetMapping("/take_Id/{playerName}")
    public int takeId(@PathVariable String playerName) {
        return playerService.takeId(playerName);
    }

    @GetMapping("/{username}")
    public Player getPlayerByUsername(@PathVariable String username) {
        return playerService.getPlayerByUsername(username);
    }
    @PostMapping("/addTeamId/{playerId}/{teamId}")
    public void addTeamId(@PathVariable Integer playerId,@PathVariable Integer teamId) {

        playerService.addTeamId(playerId,teamId);
    }
}