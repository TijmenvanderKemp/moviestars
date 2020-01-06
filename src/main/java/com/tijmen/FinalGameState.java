package com.tijmen;

public class FinalGameState extends RuntimeException {
    private final Score finalScore;

    public FinalGameState(Score score) {
        finalScore = score;
    }

    public Score getFinalScore() {
        return finalScore;
    }
}
