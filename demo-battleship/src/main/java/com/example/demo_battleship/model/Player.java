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
    @Column(name = "player_id")
    private int playerId;
    @Column(name = "player_name")
    private String playerName;

    @Column(name = "hits_count")
    private int hitsCount;

    @Column(name = "misses_count")
    private int missesCount;

    @Column(name = "wins_count")
    private int winsCount;

    @Column(name = "losses_count")
    private int lossesCount;

    @Column(name = "matches_count")
    private int matchesCount;

    @Column(name = "player_team_id")
    private int playerTeamId;

    public Player(String player2) {
        this.playerName=player2;
    }

    public void incrementHitsShots(){
        this.hitsCount++;
    }
    public void incrementMissesShots(){
        this.missesCount++;
    }
    public void incrementWins(){
        this.winsCount++;
    }
    public void incrementLosses(){
        this.lossesCount++;
    }
    public void incrementMatches(){
        this.matchesCount++;
    }
    // Getters and setters
}
