package com.tijmen;

public interface Strategy {

    /**
     * @param context@return the move that the strategy wants to make
     */
    Pair<Actor, Score> nextMove(ProblemContext context);
}
