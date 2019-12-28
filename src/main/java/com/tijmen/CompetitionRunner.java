package com.tijmen;

import java.util.Set;

public class CompetitionRunner {
    private final Reader in;
    private final Writer out;
    private Score score = new Score();
    private Player weAre;
    private Problem initialProblem;

    public CompetitionRunner(Reader reader, Writer writer) {
        this.in = reader;
        this.out = writer;
    }

    public void run() {
        HopcroftKarpParser parser = new HopcroftKarpParser();
        initialProblem = parser.parse(in);

        weAre = determineWhichPlayerWeAre();
        Pair<Player, HopcroftKarpGraph> solve = new HopcroftKarpAlgorithm(HopcroftKarpGraph.of(initialProblem)).solve();

        startPlaying(solve.getRight().getFreeWomen(), solve.getLeft());
    }

    private Player determineWhichPlayerWeAre() {
        String weAre = in.nextLine();
        return Player.parse(weAre);
    }

    private void startPlaying(Set<Actor> winningOptionsForVeronique, Player victor) {
        Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove = waitForOurFirstTurn(initialProblem, winningOptionsForVeronique);
        Strategy strategy = createStrategy(victor);

        while (true) {
            Actor nextMove = strategy.nextMove(1, problemOptionsAndMove, score).getLeft();
            if (nextMove == null) {
                out.println("I give up");
                throw new WeLost(score);
            }
            out.println(nextMove.name);
            Actor theirMove = problemOptionsAndMove.getRight();
            score = score.add(problemOptionsAndMove.getLeft().collabCount.get(theirMove).get(nextMove));
            problemOptionsAndMove = waitForOurTurn(problemOptionsAndMove.getLeft());
        }
    }

    private Triple<Problem, Set<Actor>, Actor> waitForOurFirstTurn(Problem problem, Set<Actor> winningOptions) {
        if (weAre == Player.MARK) {
            return waitForOurTurn(problem);
        } else {
            return new Triple<>(problem, winningOptions, null);
        }
    }

    private Triple<Problem, Set<Actor>, Actor> waitForOurTurn(Problem problem) {
        String theirMove = waitForMove();
        if (theirMove.equals("I give up")) {
            throw new WeWon(score);
        }
        Actor theirMoveActor = problem.actorRepository.getByName(theirMove);
        Set<Actor> ourOptions = problem.collabs.get(theirMoveActor);
        Problem newProblem = problem.withoutActor(theirMoveActor);
        return new Triple<>(newProblem, ourOptions, theirMoveActor);
    }

    private String waitForMove() {
        return in.nextLine();
    }

    Strategy createStrategy(Player victor) {
        if (weAre == victor) {
            return new FirstMoveWinningStrategyForVeronique();
        } else {
            return new LosingStrategy();
        }
    }
}
