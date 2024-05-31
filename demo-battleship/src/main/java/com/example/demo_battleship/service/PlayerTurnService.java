package com.example.demo_battleship.service;

import com.example.demo_battleship.model.PlayerTurn;
import com.example.demo_battleship.repository.PlayerTurnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerTurnService {
    @Autowired
    private PlayerTurnRepository playerTurnRepository;

    public PlayerTurn addPlayerTurn(Integer playerId, String name) {
        PlayerTurn playerTurn = new PlayerTurn();
        playerTurn.setPlayerMatchupId(playerId);
        playerTurn.setPlayerName(name);
        return playerTurnRepository.save(playerTurn);
    }

    public Optional<PlayerTurn> getPlayerTurnById(Integer id) {
        return playerTurnRepository.findById(id);
    }

    public void deletePlayerTurnById(Integer id) {
        if(playerTurnRepository.existsById(id)){
            playerTurnRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("PlayerTurn with this id does not exist");
        }
    }
}
