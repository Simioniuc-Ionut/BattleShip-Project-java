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
    private Long gameId;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private Player player1Id;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private Player player2Id;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winnerId;

    private int player1Hits;
    private int player1Misses;
    private int player2Misses;
    private int player2Hits;
    private LocalDateTime createdAt;

    // Getters and setters
}
