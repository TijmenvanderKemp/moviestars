package com.tijmen.part2;

import java.util.Map;

public class Problem {
    public final ActorRepository actorRepository;
    public final Collabs collabs;
    // This should stay the same always, also for actors that aren't in the repository
    public final Map<Actor, Map<Actor, Integer>> collabCount;

    public Problem(ActorRepository actorRepository, Map<Actor, Map<Actor, Integer>> collabCount, Collabs collabs) {
        this.actorRepository = actorRepository;
        this.collabCount = collabCount;
        this.collabs = collabs;
    }

}
