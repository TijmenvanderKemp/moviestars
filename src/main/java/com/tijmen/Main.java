package com.tijmen;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        HopcroftKarpParser parser = new HopcroftKarpParser();
        HopcroftKarpGraph graph = parser.parse(System.in);
        Player victor = new HopcroftKarpAlgorithm(graph).solve();
        System.out.println(victor.getName());
    }
}
