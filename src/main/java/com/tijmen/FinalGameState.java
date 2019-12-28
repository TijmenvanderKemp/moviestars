package com.tijmen;

public class FinalGameState extends RuntimeException {
    private Score finalScore;

    public FinalGameState(Score score) {
        finalScore = score;
    }

    public Score getFinalScore() {
        return finalScore;
    }
}
