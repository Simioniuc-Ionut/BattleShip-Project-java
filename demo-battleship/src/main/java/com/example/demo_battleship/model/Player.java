package com.example.demo_battleship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;
    private String playerName;
   //private String passwordHash;
    private int hitsCount;
    private int missesCount;
    private int winsCount;
    private int lossesCount;
    private int matchesCount;
    // Getters and setters
}
