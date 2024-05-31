package com.example.demo_battleship.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "moves")
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moveId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private String movePosition;
    private String isHit;
    private LocalDateTime moveTimestamp;

    // Getters and setters
}