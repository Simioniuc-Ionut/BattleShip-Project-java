package com.example.demo_battleship.service;

import com.example.demo_battleship.model.Game;
import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.model.Ship;
import com.example.demo_battleship.repository.GameRepository;
import com.example.demo_battleship.repository.PlayerRepository;
import com.example.demo_battleship.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public void addShip(Integer gameId, Integer playerId, Ship ship) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));

        ship.setGame(game);
        ship.setPlayer(player);
        shipRepository.save(ship);
    }

    public void sinkShip(Integer gameId, Integer playerId, String shipType) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));
        Ship ship = shipRepository.findByGameAndPlayerAndShipType(game, player, shipType);
        if (ship != null) {
            ship.setSunk(true);
            shipRepository.save(ship);
        } else {
            throw new RuntimeException("Ship not found");
        }
    }

    public List<Ship> listShips() {
        return shipRepository.findAll();
    }
}
