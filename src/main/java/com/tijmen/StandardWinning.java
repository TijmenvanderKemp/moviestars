package com.tijmen;

public class StandardWinning implements Strategy {

    @Override
    public Pair<Actor, Score> nextMove(ProblemContext context) {
        Actor moveDieIkGaDoen = context.getMatching().get(context.getTheirMove());
        return new Pair<>(moveDieIkGaDoen, context.getScore().add(context.getRelevantScores().get(moveDieIkGaDoen)));
    }
}
