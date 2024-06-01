package com.example.demo_battleship.repository;

import com.example.demo_battleship.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByGameId(Long gameId);

//    Game findByPlayerId(Long playerId);
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findByPlayer1Id(Player player1);
    List<Game> findByPlayer2Id(Player player2);
    Optional<Game> findByInProgressTrue();
}