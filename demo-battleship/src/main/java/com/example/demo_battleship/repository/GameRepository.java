package com.example.demo_battleship.repository;

import com.example.demo_battleship.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}