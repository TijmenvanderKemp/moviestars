package com.tijmen;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class WinningStrategy implements Strategy {
    int allowedDepth;
    Problem problem;
    Map<Actor, Map<Actor, Integer>> collabCount;
    Set<Actor> options;
    Actor theirMove;
    Map<Actor, Integer> relevantScores;
    PlayingLineValue score;

    @Override
    public Actor nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, PlayingLineValue score) {
        // Calculate cumulative average of our playing lines
        // Find the minimum average at each line
        // Pick line with highest minimum
        this.allowedDepth = allowedDepth;
        problem = problemOptionsAndMove.getLeft();
        collabCount = problem.collabCount;
        options = problemOptionsAndMove.getMiddle();
        if (options.isEmpty()) {
            return null;
        }
        theirMove = problemOptionsAndMove.getRight();
        relevantScores = collabCount.get(theirMove);
        this.score = score;

        Optional<Actor> bestOption = getBestMove();
        return bestOption.orElse(null);
    }

    private Optional<Actor> getBestMove() {
        if (allowedDepth <= 1) {
            return options.stream()
                    .max(Comparator.comparing(relevantScores::get));
        }
        return options.stream()
                .map(this::getBestMoveForTheOtherPlayer)
                .max(Comparator.comparing(relevantScores::get));
    }

    private Actor getBestMoveForTheOtherPlayer(Actor option) {
        return new LosingStrategy().nextMove(
                allowedDepth - 1,
                new Triple<>(problem.withoutActor(option),
                        SetUtils.remove(problem.collabs.get(option), option),
                        option),
                score.add(relevantScores.get(option)));
    }
}
