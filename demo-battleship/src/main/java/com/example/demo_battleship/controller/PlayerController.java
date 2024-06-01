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

    @PostMapping("getPlayerByUsername/{username}")
    public Player getPlayerByUsername(@PathVariable String username) {
        return playerService.getPlayerByUsername(username);
    }
    @PostMapping("/addTeamId/{playerId}/{teamId}")
    public void addTeamId(@PathVariable Integer playerId,@PathVariable Integer teamId) {

        playerService.addTeamId(playerId,teamId);
    }
    @PostMapping("/delete/playerTeamId/{playerTeamId}")
    public void deletePlayerTeamId(@PathVariable Integer playerTeamId){
       // System.out.println("Received request to delete teamId for playerTeamId: " + playerTeamId);
        playerService.deletePlayerTeamId(playerTeamId);
    }
    @PostMapping("/update/hitCounts/{playerTeamId}")
    public void incrementHitCount(@PathVariable Integer playerTeamId){
        playerService.increment(playerTeamId,"HIT");
    }
    @PostMapping("/update/missCounts/{playerTeamId}")
    public void incrementMissCount(@PathVariable Integer playerTeamId){
        playerService.increment(playerTeamId,"MISS");
    }
    @PostMapping("/update/wins/{playerTeamId}")
    public void incrementWinsCount(@PathVariable Integer playerTeamId){
        playerService.increment(playerTeamId,"WIN");
    }
    @PostMapping("/update/loses/{playerTeamId}")
        public void incrementLoses(@PathVariable Integer playerTeamId){
        playerService.increment(playerTeamId,"LOSE");
    }
    @PostMapping("/update/matches/{playerTeamId}")
    public void incrementMatches(@PathVariable Integer playerTeamId){
        playerService.increment(playerTeamId,"MATCH");
    }
    @GetMapping("take_id_by_playerTeamId/{playerTeamId}")
    public int getPlayerId(@PathVariable Integer playerTeamId){
        return playerService.getPlayerIdByPlayerTeamId(playerTeamId);
    }
}