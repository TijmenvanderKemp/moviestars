package com.tijmen;

import java.util.Comparator;
import java.util.Optional;

public class LosingStrategy implements Strategy {
    private ProblemContext context;

    @Override
    public Pair<Actor, Score> nextMove(ProblemContext context) {
        // Calculate cumulative average of our playing lines
        // Find the maximum average at each line
        // Pick line with lowest maximum
        this.context = context;
        if (context.getOptions().isEmpty()) {
            return null;
        }

        Optional<Pair<Actor, Score>> bestOption = getBestMove().map(actor -> new Pair<>(actor, context.getScore()));
        return bestOption.orElse(null);
    }

    private Optional<Actor> getBestMove() {
        if (context.getAllowedDepth() <= 1) {
            if (context.getRelevantScores() != null) {
                return context.getOptions().stream()
                        .min(Comparator.comparing(context.getRelevantScores()::get));
            } else {
                return context.getOptions().stream().findFirst();
            }
        }

        return context.getOptions().stream()
                .min(Comparator.comparing(actor -> getBesteScore(actor).getScoreSoFar()));
    }

    private Score getBesteScore(Actor actor) {
        return getBestMoveForTheOtherPlayer(actor).getRight();
    }

    private Pair<Actor, Score> getBestMoveForTheOtherPlayer(Actor option) {
        return new StandardWinning().nextMove(context.copy()
                .withAllowedDepth(context.getAllowedDepth() - 1)
                .withProblem(context.getProblem().withoutActor(option))
                .withOptions(SetUtils.remove(context.getProblem().hopcroftKarpCollabs.get(option), option))
                .withTheirMove(option)
                .withScore(getScoreOfOption(option).map(context.getScore()::add).orElse(context.getScore())));

    }

    private Optional<Integer> getScoreOfOption(Actor option) {
        if (context.getRelevantScores() == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(context.getRelevantScores().get(option));
        }
    }
}
