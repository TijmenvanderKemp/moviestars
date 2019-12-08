package com.tijmen;

import java.util.*;
import java.util.stream.Collectors;

public class AlgorithmByItself {
    private Map<SubProblem, Player>  encountered;
    private Map<Actor, Set<Actor>> collabs;
    private Set<Actor> firstOptions;

    AlgorithmByItself(Map<Actor, Set<Actor>> collabs, Set<Actor> firstOptions) {
        this.collabs = collabs;
        this.firstOptions = firstOptions;
        encountered = new HashMap<>();
    }

    public Player solve() {
        Player player = Player.VERONIQUE;

        for(Actor actor : firstOptions) {
            Set<Actor> picked = new HashSet<>();
            picked.add(actor);
            if (solve(player.next(), actor, picked).equals(player)) {
                //System.out.print("Winning pick: " + actor);
                return player;
            }
        }
        return player.next();
    }

    public Player solve(Player player, Actor actor , Set<Actor> picked) {

        //System.out.println(picked.size() + " " + player.getName()  + " " + actor + " " + picked);

        Set<Actor> allOptions = collabs.get(actor);
        List<Actor> actualOptions = allOptions.stream().filter(coworker -> !picked.contains(coworker)).sorted(Comparator.comparing(coworker -> collabs.get(coworker).size())).collect(Collectors.toList());

        if(actualOptions.isEmpty()) {
            //System.out.print(" no coworkers left ");
            return player.next();
        }

        SubProblem problem = new SubProblem(picked, actor);
        if(encountered.containsKey(problem)) {
            // System.out.print(" encountered before: " + picked + " ");
            return encountered.get(problem);
        }

        for (Actor coworker : actualOptions) {
            //System.out.println("\n" + picked.size() + " " + actor + " -> " + coworker + " / " + allOptions.stream().filter(coworker2 -> !picked.contains(coworker2)).collect(Collectors.toList()));

            Set<Actor> newPicked = new HashSet<>(picked);
            SubProblem subProblem = new SubProblem(newPicked, coworker);
            newPicked.add(coworker);
            if (solve(player.next(), coworker, newPicked).equals(player)) {
                //System.out.println(picked.size() + " Winning actor: " + coworker + " winning player: " + player.getName() + " ");
                encountered.put(subProblem, player);
                return player;
            } else {
                //System.out.println(picked.size() + " Losing actor: " + coworker + " losing player: " + player.getName() + " ");
                encountered.put(subProblem, player.next());
            }
        }

        return player.next();

    }

}
