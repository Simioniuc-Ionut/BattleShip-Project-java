package com.example.demo_battleship.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameId;

    @Column(name = "player1_id")
    private int player1Id;

    @Column(name = "player2_id")
    private int player2Id;

    @Column(name = "winner_id")
    private int winnerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "in_progress")
    private boolean inProgress;
    // Getters and setters

}
