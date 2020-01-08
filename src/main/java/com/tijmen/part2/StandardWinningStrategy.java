package com.tijmen.part2;

public class StandardWinningStrategy implements Strategy {

    @Override
    public Pair<Actor, Score> nextMove(ProblemContext context) {
        Actor moveDieIkGaDoen = context.getMatching().get(context.getTheirMove());
        if (moveDieIkGaDoen == null && !context.getOptions().isEmpty()) {
            moveDieIkGaDoen = context.getOptions().iterator().next();
        } else if (moveDieIkGaDoen == null) {
            return null;
        }

        return new Pair<>(moveDieIkGaDoen, context.getScore().add(context.getRelevantScores().get(moveDieIkGaDoen)));
    }
}
