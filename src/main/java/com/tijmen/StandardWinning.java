package com.tijmen;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardWinning implements Strategy {

    private Map<Actor, Actor> matching;

    public StandardWinning(Map<Actor, Actor> MtoFmatching, Player player) {
        if (player.equals(Player.MARK)) {
            matching = MtoFmatching.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        } else {
            matching = MtoFmatching;
        }
    }

    @Override
    public Pair<Actor, Score> nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, Score score) {
        Actor theirMove = problemOptionsAndMove.getRight();
        Map<Actor, Integer> relevantScores = problemOptionsAndMove.getLeft().collabCount.get(theirMove);
        Actor moveDieIkGaDoen = matching.get(problemOptionsAndMove.getRight());
        return new Pair<>(moveDieIkGaDoen, score.add(relevantScores.get(moveDieIkGaDoen)));
    }
}
