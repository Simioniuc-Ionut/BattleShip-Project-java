package com.example.demo_battleship.service;

import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;


    public Player addPlayer(Player player) {
        if (playerRepository.existsByPlayerName(player.getPlayerName())) {
            throw new IllegalArgumentException("Player with this username already exists");
        }
        return playerRepository.save(player);
    }
    public Player getPlayerByUsername(String username) {
        return playerRepository.findByPlayerName(username);
    }

    public Iterable<Player> listPlayers() {
        return playerRepository.findAll();
    }
}