package org.example;


public enum GameState {
    GAME_NOT_CREATED(0),
    WAITING_FOR_PLAYER(3),
    GAME_READY_TO_MOVE(4),
    PLAYER1_TURN(1),
    PLAYER2_TURN(2),
    GAME_OVER(5),
    GAME_TOURNAMENT(6);

    private final int stateCode;

    GameState(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return this.stateCode;
    }
}

