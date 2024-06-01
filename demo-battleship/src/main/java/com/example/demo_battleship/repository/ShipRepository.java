package com.example.demo_battleship.repository;

import com.example.demo_battleship.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Integer> {
}