package com.tijmen.part1;

import java.util.List;
import java.util.Set;

public class HopcroftKarpAlgorithm {
    private final HopcroftKarpGraph graph;

    public HopcroftKarpAlgorithm(HopcroftKarpGraph graph) {
        this.graph = graph;
    }

    /**
     * @return (winning player, graph)
     */
    public Pair<Player, HopcroftKarpGraph> solve() {
        Set<List<Integer>> augmentingPaths = graph.findAugmentingPaths();
        while (!augmentingPaths.isEmpty()) {
            graph.augmentGraph(augmentingPaths);
            augmentingPaths = graph.findAugmentingPaths();
        }

        if (graph.getFreeWomen().isEmpty()) {
            return new Pair<>(Player.MARK, graph);
        } else {
            return new Pair<>(Player.VERONIQUE, graph);
        }
    }

}
