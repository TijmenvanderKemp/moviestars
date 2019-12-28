package com.tijmen;

import java.util.Set;

public class LosingStrategy implements Strategy {
    @Override
    public Actor nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, PlayingLineValue score) {
        // Pick between stopping and all their best choices for each possible our choice
        return null;
    }
}
