package com.example.demo_battleship.service;

import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;


    public Player addPlayer(Player player) {

        if (playerRepository.existsByPlayerName(player.getPlayerName())) {
            System.out.println("Already exist " + player.getPlayerName());
            throw new IllegalArgumentException("Player with this username already exists");
        }
        return playerRepository.save(player);
    }

    public int takeId(String playerName) {
        Player player = playerRepository.findByPlayerName(playerName);
        if(player == null) {
            throw new IllegalArgumentException("Player with this username does not exist");
        }else {
            return player.getPlayerId();
        }
    }

    public Player getPlayerByUsername(String username) {
        return playerRepository.findByPlayerName(username);
    }

    public void addTeamId(Integer playerId, Integer teamId) {
        // Ia jucătorul din baza de date folosind playerId-ul
        Player player = playerRepository.findById(playerId).orElse(null);
        System.out.println("ADDTEAMID " + player.getPlayerName() + " " + player.getPlayerId() );

        if (player != null) {
            // Actualizează teamId-ul jucătorului
            player.setPlayerTeamId(teamId);
            // Salvează modificările în baza de date
            playerRepository.save(player);
        } else {
            // Tratează cazul în care nu există jucătorul cu playerId-ul dat
           throw new IllegalArgumentException("Player with this id does not exist");
        }
    }

    public List<Player> listPlayers() {
        return playerRepository.findAll();
    }
}