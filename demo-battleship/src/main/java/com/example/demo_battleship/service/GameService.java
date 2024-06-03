package com.example.demo_battleship.service;

import com.example.demo_battleship.model.Game;
import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.repository.GameRepository;
import com.example.demo_battleship.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<Game> listGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Integer gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }

    public Game updateGame(Game game) {
        return gameRepository.save(game);
    }


    @Transactional
    public void deleteGame(Integer gameId) {
        Game game = getGameById(gameId);
        gameRepository.delete(game);
    }

    public List<Game> getGamesByPlayerId(Integer playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        List<Game> gamesAsPlayer1 = gameRepository.findByPlayer1Id(player);
        List<Game> gamesAsPlayer2 = gameRepository.findByPlayer2Id(player);
        gamesAsPlayer1.addAll(gamesAsPlayer2);
        return gamesAsPlayer1;
    }

    public boolean checkIfGameExists() {
        return gameRepository.findByInProgressTrue().isPresent();
    }

    public Game createGame(int player1Id) {
        Game game = new Game();
        game.setPlayer1Id(player1Id);
        game.setInProgress(true);
        game.setCreatedAt(LocalDateTime.now());
        return gameRepository.save(game);
    }

    public Game updateGamePlayer2Id(int player2Id) {
        Optional<Game> gameOptional = gameRepository.findByInProgressTrue();

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            game.setPlayer2Id(player2Id);
            // game.setInProgress(false);
            return gameRepository.save(game);
        } else {
            throw new IllegalArgumentException("No game in progress");
        }
    }

    public void updateWinner(Integer playerId) {
        Optional<Game> gameOptional = gameRepository.findByInProgressTrue();
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            game.setWinnerId(playerId);
            //dupa ce am stabilit winnerul
            game.setInProgress(false);
            gameRepository.save(game);
        } else {
            throw new IllegalArgumentException("No game in progress");
        }
    }

    public int takeGameIdByInProgreesTrue() {
        Optional<Game> gameOptional = gameRepository.findByInProgressTrue();
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            return game.getGameId();
        } else {
            throw new IllegalArgumentException("No game in progress");
        }
    }
}