package com.tijmen;

import java.util.Comparator;
import java.util.Optional;

public class FirstMoveWinningStrategyForVeronique implements Strategy {
    private ProblemContext context;

    @Override
    public Pair<Actor, Score> nextMove(ProblemContext context) {
        // Calculate cumulative average of our playing lines
        // Find the minimum average at each line
        // Pick line with highest minimum
        this.context = context;
        if (context.getOptions().isEmpty()) {
            return null;
        }
        Optional<Pair<Actor, Score>> bestMoveAndScore = getBestMove().map(this::addTheScoreOfTheMove);
        return bestMoveAndScore.orElse(null);
    }

    private Pair<Actor, Score> addTheScoreOfTheMove(Actor actor) {
        return new Pair<>(actor, context.getScore());
    }

    private Optional<Actor> getBestMove() {
        if (context.getAllowedDepth() <= 1) {
            return context.getOptions().stream().findFirst();
        }

        return context.getOptions().stream()
                .max(Comparator.comparing(actor -> getBesteScore(actor).getScoreSoFar()));
    }

    private Score getBesteScore(Actor actor) {
        return getBestMoveForTheOtherPlayer(actor).getRight();
    }

    private Pair<Actor, Score> getBestMoveForTheOtherPlayer(Actor option) {
        Collabs collabs = context.getProblem().collabs;
        collabs.ignore(option);
        Pair<Actor, Score> moveFromLosingStrategy = new LosingStrategy().nextMove(context.copy()
                .withAllowedDepth(context.getAllowedDepth() - 1)
                .withProblem(context.getProblem())
                .withOptions(collabs.get(option))
                .withTheirMove(option)
                .withScore(context.getScore()));
        collabs.acknowledge(option);
        return moveFromLosingStrategy;
    }
}
