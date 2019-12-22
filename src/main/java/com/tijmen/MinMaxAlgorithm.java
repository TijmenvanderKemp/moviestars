package com.tijmen;

import java.util.*;
import java.util.stream.Collectors;

public class MinMaxAlgorithm {
    private Map<SubProblem, Player> encountered;
    private Map<Actor, Set<Actor>> collabs;
    private Set<Actor> firstOptions;
    private Set<Actor> picked;

    MinMaxAlgorithm(Map<Actor, Set<Actor>> collabs, Set<Actor> firstOptions) {
        this.collabs = collabs;
        this.firstOptions = firstOptions;
        encountered = new HashMap<>();
        picked = new HashSet<>();
    }

    public Player solve() {
        Player player = Player.VERONIQUE;

        for(Actor actor : firstOptions) {
            picked.add(actor);
            if (solve(player.next(), actor).equals(player)) {
                //System.out.print("Winning pick: " + actor);
                return player;
            }
        }
        return player.next();
    }

    public Player solve(Player player, Actor actor) {


        Set<Actor> allOptions = collabs.get(actor);
        List<Actor> actualOptions = allOptions.stream().filter(coworker -> !picked.contains(coworker)).sorted(Comparator.comparing(coworker -> collabs.get(coworker).size())).collect(Collectors.toList());

        if(actualOptions.isEmpty()) {
            return player.next();
        }

        SubProblem problem = new SubProblem(picked, actor);
        if(encountered.containsKey(problem)) {
            return encountered.get(problem);
        }

        for (Actor coworker : actualOptions) {

            picked.add(coworker);
            if (solve(player.next(), coworker).equals(player)) {
                picked.remove(coworker);
                return player;
            } else {
                picked.remove(coworker);
                SubProblem subProblem = new SubProblem(new HashSet<>(picked) , coworker);
                encountered.put(subProblem, player.next());
            }
        }

        return player.next();

    }

}
