package com.tijmen;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardWinning implements Strategy {

    @Override
    public Pair<Actor, Score> nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, Score score, Map<Actor, Actor> matching, Player player) {
        Actor theirMove = problemOptionsAndMove.getRight();
        Map<Actor, Integer> relevantScores = problemOptionsAndMove.getLeft().collabCount.get(theirMove);
        Actor moveDieIkGaDoen = matching.get(problemOptionsAndMove.getRight());
        return new Pair<>(moveDieIkGaDoen, score.add(relevantScores.get(moveDieIkGaDoen)));
    }
}
