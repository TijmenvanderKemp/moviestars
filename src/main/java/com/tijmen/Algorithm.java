package com.tijmen;

import java.util.*;

public class Algorithm {
    private Graph graph;

    Algorithm(Graph graph) {
        this.graph = graph;
    }

    public Player solve() {
    Optional<LinkedList<Actor>> augmentingPath = graph.augmentingPathExists();
    while(augmentingPath.isPresent()) {
        graph.augmentGraph(augmentingPath.get());
        augmentingPath = graph.augmentingPathExists();
    }

    if(graph.getFreeWomen().isEmpty()) {
        return Player.MARK;
    } else {
        return Player.VERONIQUE;
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
