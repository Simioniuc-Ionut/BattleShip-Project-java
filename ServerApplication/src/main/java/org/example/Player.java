package org.example;

import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private final int id;
    @Setter
    private Set<Integer> matchesRemaining;

    public Player(int id) {
        this.id = id;
        this.matchesRemaining = new HashSet<>();
    }
    public void addMatch(int matchId){
        this.matchesRemaining.add(matchId);
    }

    public Set<Integer> getMatchesRemaining() {
        return this.matchesRemaining;
    }

    public Integer getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", matchesRemaining=" + matchesRemaining +
                '}';
    }
}
