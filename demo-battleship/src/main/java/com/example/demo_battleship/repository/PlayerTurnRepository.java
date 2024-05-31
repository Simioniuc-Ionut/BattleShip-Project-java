package com.example.demo_battleship.repository;

import com.example.demo_battleship.model.PlayerTurn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerTurnRepository extends JpaRepository<PlayerTurn, Integer> {

}
