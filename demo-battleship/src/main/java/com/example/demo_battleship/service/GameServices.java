package com.example.demo_battleship.service;

import com.example.demo_battleship.model.Game;
import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServices {
    @Autowired
    private GameRepository gameRepository;
    public List<Game> listGames() {
        return gameRepository.findAll();
    }

//    public Game addGame(Game game) {
//
//        if (gameRepository.existsByGameId(game.getGameId())) {
//            System.out.println("Already exist " + game.getGameId());
//            throw new IllegalArgumentException("Player with this username already exists");
//        }
//        return gameRepository.save(game);
//    }

//    public int takeId(Long player1Id, Long player2Id) {
//        Game player1 = gameRepository.findByPlayerId(player1Id);
//        Game player2 = gameRepository.findByPlayerId(player2Id);
//        if((player1 == null) && (player2 == null)) {
//            throw new IllegalArgumentException("Game with the 2 players doesn't exist");
//        }else {
//            return addGame().getGameId();
//        }
//    }

}
