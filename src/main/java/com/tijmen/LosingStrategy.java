package com.tijmen;

import java.util.Map;
import java.util.Set;

public class LosingStrategy implements Strategy {
    private StandardWinning winning;

    @Override
    public Pair<Actor, Score> nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, Score score, Map<Actor, Actor> matching, Player player) {
        // Pick between stopping and all their best choices for each possible our choice
        return null;
    }
}
