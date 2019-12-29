package com.tijmen;

import java.util.Map;
import java.util.Set;

public class Problem {
    public ActorRepository actorRepository;
    public Map<Actor, Set<Actor>> hopcroftKarpCollabs;
    // This should stay the same always, also for actors that aren't in the repository
    public Map<Actor, Map<Actor, Integer>> collabCount;

    public Problem(ActorRepository actorRepository, Map<Actor, Set<Actor>> hopcroftKarpCollabs, Map<Actor, Map<Actor, Integer>> collabCount) {
        this.actorRepository = actorRepository;
        this.hopcroftKarpCollabs = hopcroftKarpCollabs;
        this.collabCount = collabCount;
    }

    public Problem withoutActor(Actor actor) {
        ActorRepository newRepo = actorRepository.remove(actor);
        Map<Actor, Set<Actor>> collabs = MapUtils.removeWithSet(this.hopcroftKarpCollabs, actor);
        return new Problem(newRepo, collabs, collabCount);
    }

    public void removeActor(Actor actor) {
        actorRepository.remove(actor);
        hopcroftKarpCollabs.remove(actor);
        for (Set<Actor> coworkers : hopcroftKarpCollabs.values()) {
            coworkers.remove(actor);
        }
        collabCount.remove(actor);
        for (Map<Actor, Integer> scores : collabCount.values()) {
            scores.remove(actor);
        }
    }
}
