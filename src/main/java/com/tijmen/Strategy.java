package com.tijmen;

import java.util.Map;
import java.util.Set;

public interface Strategy {

    /**
     * @param allowedDepth          How far the strategy is allowed to look for a good move
     * @param problemOptionsAndMove The move last made by the opponent. Use null to indicate that we're making the first move
     * @param score                 The score that the winning player so far has managed to acquire
     * @return the move that the strategy wants to make
     */
    Pair<Actor, Score> nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, Score score, Map<Actor, Actor> matching, Player player);
}
