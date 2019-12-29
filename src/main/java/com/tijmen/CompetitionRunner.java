package com.tijmen;

import java.util.Map;
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
        Map<Actor, Actor> matching;
        if (weAre == Player.MARK) {
            matching = solve.getRight().getF2mMatching();
        } else {
            matching = solve.getRight().getM2fMatching();
        }

        startPlaying(solve.getRight().getFreeWomen(), solve.getLeft(), matching);
    }

    private Player determineWhichPlayerWeAre() {
        String weAre = in.nextLine();
        return Player.parse(weAre);
    }

    private void startPlaying(Set<Actor> winningOptionsForVeronique, Player victor, Map<Actor, Actor> matching) {
        Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove = waitForOurFirstTurn(initialProblem, winningOptionsForVeronique);
        Strategy strategy = createStrategy(victor);

        while (true) {
            Problem problem = problemOptionsAndMove.getLeft();
            Set<Actor> options = problemOptionsAndMove.getMiddle();
            Actor theirMove = problemOptionsAndMove.getRight();
            ProblemContext context = new ProblemContext()
                    .withProblem(problem)
                    .withOptions(options)
                    .withTheirMove(theirMove)
                    .withAllowedDepth(1)
                    .withScore(score)
                    .withMatching(matching);

            Pair<Actor, Score> actorScorePair = strategy.nextMove(context);
            if (actorScorePair == null) {
                out.println("I give up");
                break;
            }
            Actor ourMove = actorScorePair.getLeft();
            out.println(ourMove.name);
            problem.collabs.ignore(ourMove);
            score = calculateScoreOfOurMove(problem, theirMove, ourMove);
            problemOptionsAndMove = waitForOurTurn(problem);
            if (strategy instanceof FirstMoveWinningStrategyForVeronique) {
                strategy = new StandardWinning();
            }
        }
        throw new WeLost(score);
    }

    private Score calculateScoreOfOurMove(Problem problem, Actor theirMove, Actor ourMove) {
        if (problem.collabCount.get(theirMove) == null) {
            // Dit is de allereerste move dus er zit geen score aan die move.
            return score;
        }
        return score.add(problem.collabCount.get(theirMove).get(ourMove));
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
        problem.collabs.ignore(theirMoveActor);
        Set<Actor> ourOptions = problem.collabs.get(theirMoveActor);
        Problem newProblem = problem.withoutActor(theirMoveActor);
        return new Triple<>(newProblem, ourOptions, theirMoveActor);
    }

    private String waitForMove() {
        return in.nextLine();
    }

    Strategy createStrategy(Player victor) {
        if (weAre == victor) {
            if (weAre == Player.VERONIQUE) {
                return new FirstMoveWinningStrategyForVeronique();
            }
            return new StandardWinning();
        } else {

            return new LosingStrategy();
        }
    }
}
