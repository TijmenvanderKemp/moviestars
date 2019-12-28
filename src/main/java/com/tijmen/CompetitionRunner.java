package com.tijmen;

import java.util.Set;

public class CompetitionRunner {
    private final Reader in;
    private final Writer out;
    private PlayingLineValue score = new PlayingLineValue();

    public CompetitionRunner(Reader reader, Writer writer) {
        this.in = reader;
        this.out = writer;
    }

    public void run() {
        HopcroftKarpParser parser = new HopcroftKarpParser();
        Problem problem = parser.parse(in);

        Player weAre = determineWhichPlayerWeAre();
        startPlaying(problem, weAre);
    }

    private void startPlaying(Problem problem, Player weAre) {
        Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove = waitForOurFirstTurn(problem, weAre);
        Strategy strategy = computeStrategy(weAre, problem);

        while (true) {
            Actor nextMove = strategy.nextMove(1, problemOptionsAndMove, score);
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

    private Triple<Problem, Set<Actor>, Actor> waitForOurFirstTurn(Problem problem, Player weAre) {
        if (weAre == Player.MARK) {
            return waitForOurTurn(problem);
        } else {
            return new Triple<>(problem, problem.actorRepository.femaleActors, null);
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

    private Strategy computeStrategy(Player weAre, Problem problem) {
        Pair<Player, HopcroftKarpGraph> solve = new HopcroftKarpAlgorithm(HopcroftKarpGraph.of(problem)).solve();
        Player victor = solve.getLeft();
        return createStrategy(weAre, victor);
    }

    Strategy createStrategy(Player weAre, Player victor) {
        if (weAre == victor) {
            return new WinningStrategy();
        } else {
            return new LosingStrategy();
        }
    }

    private Player determineWhichPlayerWeAre() {
        String weAre = in.nextLine();
        return Player.parse(weAre);
    }
}
