package com.tijmen;

public class Problem {
    private final Graph graph;
    private final int numberOfBins;

    public Problem(Graph graph, int numberOfBins) {
        this.graph = graph;
        this.numberOfBins = numberOfBins;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getNumberOfBins() {
        return numberOfBins;
    }
}
