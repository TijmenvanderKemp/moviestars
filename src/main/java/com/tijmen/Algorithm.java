package com.tijmen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

        Iterator<Actor> iterator = options.iterator();
        while(iterator.hasNext()) {

            Actor actor = iterator.next();
            Map<Actor, Set<Actor>> collabsWithoutActor = new HashMap<>(problem.getCollabs());
            Set<Actor> coworkers = collabsWithoutActor.get(actor);
            coworkers.stream().forEach(actor2 -> collabsWithoutActor.get(actor2).remove(actor));
            collabsWithoutActor.remove(actor);
            Problem subProblem = new Problem(player.next(), coworkers, collabsWithoutActor);
            Algorithm subalgorithm = new Algorithm(subProblem, encountered);

            if(subalgorithm.solve().equals(player)) {
                return player;
            } else {
                encountered.put(problem, player.next());
            }
        }

        return player.next();

    }

}
