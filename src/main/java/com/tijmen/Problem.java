package com.tijmen;

import java.util.Map;
import java.util.Set;

public class Problem {
    public ActorRepository actorRepository;
    public Map<Actor, Set<Actor>> collabs;
    // This should stay the same always, also for actors that aren't in the repository
    public Map<Actor, Map<Actor, Integer>> collabCount;

    public Problem(ActorRepository actorRepository, Map<Actor, Set<Actor>> collabs, Map<Actor, Map<Actor, Integer>> collabCount) {
        this.actorRepository = actorRepository;
        this.collabs = collabs;
        this.collabCount = collabCount;
    }

    public Problem withoutActor(Actor actor) {
        ActorRepository newRepo = actorRepository.remove(actor);
        Map<Actor, Set<Actor>> collabs = MapUtils.removeWithSet(this.collabs, actor);
        return new Problem(newRepo, collabs, collabCount);
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
