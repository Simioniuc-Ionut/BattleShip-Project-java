package com.example.demo_battleship.repository;

import com.example.demo_battleship.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByPlayerName(String playerName);
    boolean existsByPlayerName(String playerName);

    Player findByPlayerTeamId(Integer playerTeamId);
}