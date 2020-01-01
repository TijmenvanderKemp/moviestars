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

        Optional<Pair<Actor, Double>> min = context.getOptions().stream()
                .map(actor -> new Pair<>(actor, getBesteScore(actor).getScoreSoFar()))
                .min(Comparator.comparing(pair -> pair.getRight()));
        Optional<Actor> bestActor = Optional.empty();
        if(min.isPresent()) {
            double scoreSoFar = context.getScore().getScoreSoFar();
            if( scoreSoFar == 0 ||  min.get().getRight() < scoreSoFar ) {
                bestActor = Optional.of(min.get().getLeft());
            }
        }
        return bestActor;
    }

    private Score getBesteScore(Actor actor) {
        Pair<Actor, Score> best = getBestMoveForTheOtherPlayer(actor);
        if(best == null) {
            return context.getScore();
        }
        return getBestMoveForTheOtherPlayer(actor).getRight();
    }

    private Pair<Actor, Score> getBestMoveForTheOtherPlayer(Actor option) {
        Collabs collabs = context.getProblem().collabs;
        collabs.ignore(option);
        Pair<Actor, Score> winningStrategyNextMove = new StandardWinning().nextMove(context.copy()
                .withAllowedDepth(context.getAllowedDepth() - 1)
                .withProblem(context.getProblem())
                .withOptions(collabs.get(option))
                .withTheirMove(option)
                .withScore(context.getScore()));
        collabs.acknowledge(option);
        return winningStrategyNextMove;

    }
}
