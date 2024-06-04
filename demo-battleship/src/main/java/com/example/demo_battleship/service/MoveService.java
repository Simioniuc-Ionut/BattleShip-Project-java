package com.example.demo_battleship.service;

import com.example.demo_battleship.model.Game;
import com.example.demo_battleship.model.Move;
import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.repository.GameRepository;
import com.example.demo_battleship.repository.MoveRepository;
import com.example.demo_battleship.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveService {

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public void recordMove(Integer gameId, Integer playerId, String move, boolean isHit) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));

        Move newMove = new Move();
        newMove.setGame(game);
        newMove.setPlayer(player);
        newMove.setMove(move);
        newMove.setIsHit(isHit);

        moveRepository.save(newMove);//salvare in bd
    }

    public List<Move> listMoves() {
        return moveRepository.findAll();
    }
}