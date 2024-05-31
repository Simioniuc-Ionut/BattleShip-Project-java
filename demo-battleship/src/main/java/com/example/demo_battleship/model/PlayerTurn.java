package com.example.demo_battleship.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "player_turn")
public class PlayerTurn {

    @Id
    @JoinColumn(name = "player_matchup_id")
    private int playerMatchupId;

    @Column(name = "player_name")
    private String playerName;



}
