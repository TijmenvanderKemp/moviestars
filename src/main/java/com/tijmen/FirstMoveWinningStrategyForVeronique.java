package com.tijmen;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FirstMoveWinningStrategyForVeronique implements Strategy {
    int allowedDepth;
    Problem problem;
    Map<Actor, Map<Actor, Integer>> collabCount;
    Set<Actor> options;
    Actor theirMove;
    Map<Actor, Integer> relevantScores;
    Score score;

    @Override
    public Pair<Actor, Score> nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, Score score) {
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

        Optional<Pair<Actor, Score>> bestOption = getBestMove().map(actor -> new Pair<>(actor, score.add(relevantScores.get(actor))));
        return bestOption.orElse(null);
    }

    private Optional<Actor> getBestMove() {
        if (allowedDepth <= 1) {
            return options.stream()
                    .max(Comparator.comparing(relevantScores::get));
        }

        return options.stream()
                .max(Comparator.comparing(actor -> getBesteScore(actor).getScoreSoFar()));
    }

    private Score getBesteScore(Actor actor) {
        return getBestMoveForTheOtherPlayer(actor).getRight();
    }

    private Pair<Actor, Score> getBestMoveForTheOtherPlayer(Actor option) {
        return new LosingStrategy().nextMove(
                allowedDepth - 1,
                new Triple<>(problem.withoutActor(option),
                        SetUtils.remove(problem.collabs.get(option), option),
                        option),
                score.add(relevantScores.get(option)));
    }
}
