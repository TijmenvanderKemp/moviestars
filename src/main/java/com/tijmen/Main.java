package com.tijmen;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        HopcroftKarpParser parser = new HopcroftKarpParser();
        Problem problem = parser.parse(System.in);
        HopcroftKarpGraph graph = HopcroftKarpGraph.of(problem);
        Player victor = new HopcroftKarpAlgorithm(graph).solve();
        System.out.println(victor.getName());
    }
}
