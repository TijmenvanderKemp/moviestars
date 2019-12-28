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
        Problem problem;
        Set<Actor> options;
        Actor theirMove;
        Strategy strategy = createStrategy(victor);

        while (true) {
            problem = problemOptionsAndMove.getLeft();
            options = problemOptionsAndMove.getMiddle();
            theirMove = problemOptionsAndMove.getRight();
            ProblemContext context = new ProblemContext()
                    .withProblem(problem)
                    .withOptions(options)
                    .withTheirMove(theirMove)
                    .withAllowedDepth(1)
                    .withScore(score)
                    .withMatching(matching);

            Actor nextMove = strategy.nextMove(context).getLeft();
            if (nextMove == null) {
                out.println("I give up");
                break;
            }
            out.println(nextMove.name);
            score = score.add(problem.collabCount.get(theirMove).get(nextMove));
            problemOptionsAndMove = waitForOurTurn(problem);
            if(strategy instanceof FirstMoveWinningStrategyForVeronique) {
                strategy = new StandardWinning();
            }
        }
        throw new WeLost(score);
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
            if (weAre == Player.VERONIQUE) {
                return new FirstMoveWinningStrategyForVeronique();
            }
            return new StandardWinning();
        } else {

            return new LosingStrategy();
        }
    }
}
