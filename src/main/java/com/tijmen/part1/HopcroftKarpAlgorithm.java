package com.tijmen.part1;

import java.util.LinkedList;
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
        Set<LinkedList<Actor>> augmentingPaths = graph.findAugmentingPaths();
        while (!augmentingPaths.isEmpty()) {
            graph.augmentGraph(augmentingPaths);
            augmentingPaths = graph.findAugmentingPaths();
        }

        graph.calculateMatching();

        if (graph.getFreeWomen().isEmpty()) {
            return new Pair<>(Player.MARK, graph);
        } else {
            return new Pair<>(Player.VERONIQUE, graph);
        }
        /*
    Hopcroft-Karp algorithm for finding maximal matching in bipartite graph:
    1) Initialize Maximal Matching M as empty.
    2) While there exists an Augmenting Path p
     Remove matching edges of p from M and add not-matching edges of p to M
     (This increases size of M by 1 as p starts and ends with a free vertex)
    3) Return M.
    */

    }

}
