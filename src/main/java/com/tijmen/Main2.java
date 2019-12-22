package com.tijmen;

import java.util.Scanner;
import java.util.Set;

public class Main2 {
    public static void main(String[] args) {
        HopcroftKarpParser parser = new HopcroftKarpParser();
        Problem problem = parser.parse(System.in);
        Scanner in = new Scanner(System.in);

        Player weAre = determineWhichPlayerWeAre(in);
        Set<Actor> ourOptions;
        if (weAre == Player.MARK) {
            String theirMove = waitForMove(in);
            Actor theirMoveActor = problem.actorRepository.getByName(theirMove);
            ourOptions = problem.collabs.get(theirMoveActor);
            problem.removeActor(theirMoveActor);
        } else {
            ourOptions = problem.actorRepository.femaleActors;
        }
        while (true) {
            Strategy strategy = computeStrategy(weAre, problem);
            System.out.println(strategy.nextMove().name);
        }
    }

    private static Strategy computeStrategy(Player weAre, Problem problem) {
        Player victor = new HopcroftKarpAlgorithm(HopcroftKarpGraph.of(problem)).solve();
        if (weAre == victor) {
            return new WinningStrategy();
        } else {
            return new LosingStrategy();
        }
    }

    private static String waitForMove(Scanner in) {
        return in.nextLine();
    }

    private static Player determineWhichPlayerWeAre(Scanner in) {
        String weAre = in.nextLine();
        return Player.valueOf(weAre);
    }
}
