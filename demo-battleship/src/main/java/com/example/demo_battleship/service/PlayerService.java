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
        System.out.println("addTeamId " + player.getPlayerName() + " " + player.getPlayerId() );

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

    public void deletePlayerTeamId(Integer playerTeamId){
        Player player = playerRepository.findByPlayerTeamId(playerTeamId);
        //System.out.println("PLayer team id  ,with playerId" + player.getPlayerId() + " will be deleted");
        if(player != null){
            // Actualizează teamId-ul jucătorului
            player.setPlayerTeamId(0);
            // Salvează modificările în baza de date
            playerRepository.save(player);
            //System.out.println("Player teamId updated successfully for playerId: " + player.getPlayerId());
        } else {
            // Tratează cazul în care nu există jucătorul cu playerId-ul dat
            throw new IllegalArgumentException("Player with id " + player.getPlayerId() + " does not exist");
        }
    }

    public void increment(Integer playerTeamId,String Hit){
        Player player = playerRepository.findByPlayerTeamId(playerTeamId);

        if(player != null){

            if(Hit.equals("HIT")) {
                //incrementez hiturile
                player.incrementHitsShots();
            }else if(Hit.equals("MISS")){
                player.incrementMissesShots();
            }

            // Salvează modificările în baza de date
            playerRepository.save(player);
            //System.out.println("Player teamId updated successfully for playerId: " + player.getPlayerId());
        } else {
            // Tratează cazul în care nu există jucătorul cu playerId-ul dat
            throw new IllegalArgumentException("Player with id " + player.getPlayerId() + " does not exist");
        }

    }
}