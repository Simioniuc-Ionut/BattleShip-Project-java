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
        if (player == null) {
            throw new IllegalArgumentException("Player with this username does not exist");
        } else {
            return player.getPlayerId();
        }
    }

    public Player getPlayerByUsername(String username) {
        return playerRepository.findByPlayerName(username);
    }

    public void addTeamId(Integer playerId, Integer teamId) {
        // Ia jucătorul din baza de date folosind playerId-ul
        Player player = playerRepository.findById(playerId).orElse(null);
        System.out.println("addTeamId " + player.getPlayerName() + " " + player.getPlayerId());

        if (player != null) {
            // Actualizează teamId-ul jucătorului
            player.setPlayerTeamId(teamId);
            // Salveaza modificarile în baza de date
            playerRepository.save(player);
        } else {
            // Trateaza cazul în care nu exista jucătorul cu playerId-ul dat
            throw new IllegalArgumentException("Player with this id does not exist");
        }
    }

    public List<Player> listPlayers() {
        return playerRepository.findAll();
    }

    public void deletePlayerTeamId(Integer playerTeamId) {
        Player player = playerRepository.findByPlayerTeamId(playerTeamId);
        //System.out.println("PLayer team id  ,with playerId" + player.getPlayerId() + " will be deleted");
        if (player != null) {
            // Actualizeaza teamId-ul jucatorului
            player.setPlayerTeamId(0);
            // Salveaza modificarile in baza de date
            playerRepository.save(player);
            //System.out.println("Player teamId updated successfully for playerId: " + player.getPlayerId());
        } else {
            // Trateaza cazul in care nu exista jucatorul cu playerId-ul dat
            throw new IllegalArgumentException("Player with id " + player.getPlayerId() + " does not exist");
        }
    }

    public void increment(Integer playerTeamId, String Hit) {
        Player player = playerRepository.findByPlayerTeamId(playerTeamId);

        if (player != null) {

            if (Hit.equals("HIT")) {
                //incrementez hiturile
                player.incrementHitsShots();
            } else if (Hit.equals("MISS")) {
                player.incrementMissesShots();
            } else if (Hit.equals("WIN")) {
                player.incrementWins();
            } else if (Hit.equals("LOSE")) {
                player.incrementLosses();
            } else if (Hit.equals("MATCH")) {
                player.incrementMatches();
            }

            // Salveaza modificarile in baza de date
            playerRepository.save(player);
            //System.out.println("Player teamId updated successfully for playerId: " + player.getPlayerId());
        } else {
            // Tratez cazul in care nu exista jucatorul cu playerId-ul dat
            throw new IllegalArgumentException("Player with id " + player.getPlayerId() + " does not exist");
        }

    }

    public int getPlayerIdByPlayerTeamId(Integer playerTeamId) {
        Player player = playerRepository.findByPlayerTeamId(playerTeamId);
        if (player != null) {
            //l am gasit
            return player.getPlayerId();
        } else {
            throw new IllegalArgumentException("PLayer with playerTeamId " + playerTeamId + " not found");
        }
    }
}