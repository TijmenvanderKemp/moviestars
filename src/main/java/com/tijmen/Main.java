package com.tijmen;

import com.tijmen.hopcroftkarp.HopcroftKarpAlgorithm;
import com.tijmen.hopcroftkarp.HopcroftKarpGraph;
import com.tijmen.hopcroftkarp.HopcroftKarpParser;

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
