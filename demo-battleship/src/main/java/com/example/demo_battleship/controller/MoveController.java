package com.example.demo_battleship.controller;

import com.example.demo_battleship.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moves")
public class MoveController {

    @Autowired
    private MoveService moveService;

    @PostMapping("/record/{gameId}/{playerId}")
    public void recordMove(@PathVariable Integer gameId, @PathVariable Integer playerId, @RequestParam String move, @RequestParam boolean isHit) {
        moveService.recordMove(gameId, playerId, move, isHit);
    }
}
