package com.tijmen;

public class FinalGameState extends RuntimeException {
    private PlayingLineValue finalScore;

    public FinalGameState(PlayingLineValue score) {
        finalScore = score;
    }

    public PlayingLineValue getFinalScore() {
        return finalScore;
    }
}
