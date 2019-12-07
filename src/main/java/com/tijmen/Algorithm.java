package com.tijmen;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {
    private Problem problem;
    private Map<Problem, Player>  encountered;

    Algorithm(Problem problem) {
        this.problem = problem;
        encountered = new HashMap<>();
    }

    Algorithm(Problem problem, Map<Problem, Player> encountered) {
        this.problem = problem;
        this.encountered = encountered;
    }

    public Player solve() {

        Set<Actor> options = problem.getMoveOptions();
        Player player = problem.getToMove();

        if(options.isEmpty()) {
            return player.next();
        }
        if(encountered.containsKey(problem)) {
            return encountered.get(problem);
        }

        for (Actor actor : options) {

            Map<Actor, Set<Actor>> collabsWithoutActor = problem.getCollabs().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> new HashSet<>(e.getValue())));
            Set<Actor> coworkers = collabsWithoutActor.get(actor);
            coworkers.forEach(actor2 -> collabsWithoutActor.get(actor2).remove(actor));
            collabsWithoutActor.remove(actor);
            Problem subProblem = new Problem(player.next(), coworkers, collabsWithoutActor);
            Algorithm subalgorithm = new Algorithm(subProblem, encountered);

            if (subalgorithm.solve().equals(player)) {
                return player;
            } else {
                encountered.put(problem, player.next());
            }
        }

        return player.next();

    }

}
