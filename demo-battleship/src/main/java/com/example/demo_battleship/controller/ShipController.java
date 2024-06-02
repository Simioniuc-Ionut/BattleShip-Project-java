package com.example.demo_battleship.controller;

import com.example.demo_battleship.model.Ship;
import com.example.demo_battleship.service.ShipService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
@RestController
@RequestMapping("/api/ships")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @PostMapping("/addShip/{gameId}/{playerId}")
    public void addShip(@PathVariable Integer gameId, @PathVariable Integer playerId, @RequestBody Ship ship) {
        shipService.addShip(gameId, playerId, ship);
    }


    @PostMapping("/sinkShip/{gameId}/{playerId}/{shipType}")
    public void sinkShip(@PathVariable Integer gameId, @PathVariable Integer playerId, @PathVariable String shipType) {
        shipService.sinkShip(gameId, playerId, shipType);
    }
}
