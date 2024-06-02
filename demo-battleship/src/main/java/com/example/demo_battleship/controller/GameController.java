package com.example.demo_battleship.controller;

import com.example.demo_battleship.model.Game;
import com.example.demo_battleship.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/list")
    public List<Game> listGames() {
        return gameService.listGames();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Integer gameId) {
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Game>> getGamesByPlayerId(@PathVariable Integer playerId) {
        List<Game> games = gameService.getGamesByPlayerId(playerId);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/checkGameExists")
    public boolean checkIfGameExists() {
        return gameService.checkIfGameExists();
    }

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game createdGame = gameService.createGame(game.getPlayer1Id());
        return ResponseEntity.ok(createdGame);
    }

    @PostMapping("/update/player2Id/{player2Id}")
    public ResponseEntity<Game> updateGame(@PathVariable Integer player2Id) {
        Game updatedGame = gameService.updateGamePlayer2Id(player2Id);
        if (updatedGame != null) {
            return ResponseEntity.ok(updatedGame);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update/winnerId/{playerId}")
    public void updateWinner(@PathVariable Integer playerId){
        gameService.updateWinner(playerId);
    }

    @GetMapping("/take_game_id")
    public int takeGameIdByInProgreesTrue() {
        return gameService.takeGameIdByInProgreesTrue();
    }

}
