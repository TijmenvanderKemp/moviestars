package com.tijmen;

public class PlayingLineValue {
    private double scoreSoFar = 0d;
    private int weight = 0;

    public PlayingLineValue() {
    }

    private PlayingLineValue(double scoreSoFar, int weight) {
        this.scoreSoFar = scoreSoFar;
        this.weight = weight;
    }

    public PlayingLineValue add(int score) {
        return new PlayingLineValue((scoreSoFar * weight + score) / (weight + 1), weight + 1);
    }

    public double getScoreSoFar() {
        return scoreSoFar;
    }

    int getWeight() {
        return weight;
    }
}
