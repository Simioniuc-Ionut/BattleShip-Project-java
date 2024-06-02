package com.example.demo_battleship.controller;

import com.example.demo_battleship.model.Move;
import com.example.demo_battleship.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moves")
public class MoveController {

    @Autowired
    private MoveService moveService;

    @GetMapping("/list")
        public List<Move> listMoves() {
        return moveService.listMoves();
    }
    @PostMapping("/record/{gameId}/{playerId}")
    public void recordMove(@PathVariable Integer gameId, @PathVariable Integer playerId, @RequestParam String move, @RequestParam boolean isHit) {
        moveService.recordMove(gameId, playerId, move, isHit);
    }
}
