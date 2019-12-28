package com.tijmen;

import java.util.Set;

public class LosingStrategy implements Strategy {
    @Override
    public Pair<Actor, Score> nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, Score score) {
        // Pick between stopping and all their best choices for each possible our choice
        return null;
    }
}
