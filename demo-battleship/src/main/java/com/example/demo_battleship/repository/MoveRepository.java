package com.example.demo_battleship.repository;

import com.example.demo_battleship.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepository extends JpaRepository<Move, Integer> {
}