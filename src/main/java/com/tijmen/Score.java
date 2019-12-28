package com.tijmen;

public class Score {
    private double scoreSoFar = 0d;
    private int weight = 0;

    public Score() {
    }

    private Score(double scoreSoFar, int weight) {
        this.scoreSoFar = scoreSoFar;
        this.weight = weight;
    }

    public Score add(int score) {
        return new Score((scoreSoFar * weight + score) / (weight + 1), weight + 1);
    }

    public double getScoreSoFar() {
        return scoreSoFar;
    }

    int getWeight() {
        return weight;
    }
}
