package org.example;


public enum GameState{
    GAME_NOT_CREATED(0),
    WAITING_FOR_PLAYER(3),
    GAME_READY_TO_MOVE(4),
    PLAYER1_TURN(1),
    PLAYER2_TURN(2),
    GAME_OVER(5);

    private int stateCode;

    GameState(int stateCode) {
        this.stateCode = stateCode;
    }
    public void setGameNotCreated() {
        this.stateCode = 0;
    }
    public void setWaitingForPlayer() {
        this.stateCode = 3;
    }
    public void setGameReadyToMove() {
        this.stateCode = 4;
    }
    public void setPlayer1Turn() {
        this.stateCode = 1;
    }
    public void setPlayer2Turn() {
        this.stateCode = 2;
    }
    public void setGameOver() {
        this.stateCode = 5;
    }

    public int getStateCode() {
        return this.stateCode;
    }
}

