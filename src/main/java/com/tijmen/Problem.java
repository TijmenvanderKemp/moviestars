package com.tijmen;

import java.util.Map;
import java.util.Set;

public class Problem {
    public ActorRepository actorRepository;
    public Map<Actor, Set<Actor>> collabs;
    public Map<Actor, Map<Actor, Integer>> collabCount;

    public Problem(ActorRepository actorRepository, Map<Actor, Set<Actor>> collabs, Map<Actor, Map<Actor, Integer>> collabCount) {
        this.actorRepository = actorRepository;
        this.collabs = collabs;
        this.collabCount = collabCount;
    }

    public void removeActor(Actor actor) {
        actorRepository.remove(actor);
        collabs.remove(actor);
        for (Set<Actor> coworkers : collabs.values()) {
            coworkers.remove(actor);
        }
        collabCount.remove(actor);
        for (Map<Actor, Integer> scores : collabCount.values()) {
            scores.remove(actor);
        }
    }
}
