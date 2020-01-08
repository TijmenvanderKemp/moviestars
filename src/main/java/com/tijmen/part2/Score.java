package com.tijmen.part2;

public class Score {
    private double scoreSoFar = 0d;
    private int weight = 0;
    private int initialMaxScore;

    public Score(int initialMaxScore) {
        this.initialMaxScore = initialMaxScore;
    }

    private Score(double scoreSoFar, int weight) {
        this.scoreSoFar = scoreSoFar;
        this.weight = weight;
    }

    public Score add(int score) {
        return new Score((scoreSoFar * weight + score) / (weight + 1), weight + 1);
    }

    public double getScoreSoFar() {
        return scoreSoFar == 0 ? initialMaxScore : scoreSoFar;
    }

    int getWeight() {
        return weight;
    }
}
