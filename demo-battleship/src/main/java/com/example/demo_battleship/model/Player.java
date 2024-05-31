package com.example.demo_battleship.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;
    private String playerName;
   //private String passwordHash;
    private int hitsCount;
    private int missesCount;
    private int winsCount;
    private int lossesCount;
    private int matchesCount;
    private int playerTeamId;

    public Player(String player2) {
        this.playerName=player2;
    }


    // Getters and setters
}
