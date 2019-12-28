package com.tijmen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ActorRepository {
    public Set<Actor> femaleActors = new HashSet<>();
    public Set<Actor> maleActors = new HashSet<>();
    public Map<String, Integer> customHashes = new HashMap<>();

    public Actor getByName(String name) {
        return new Actor(name, customHashes.get(name));
    }

    public Set<Actor> getAllActors() {
        Set<Actor> allActors = new HashSet<>(femaleActors);
        allActors.addAll(maleActors);
        return allActors;
    }

    public ActorRepository remove(Actor actor) {
        ActorRepository newRepo = new ActorRepository();
        newRepo.maleActors = SetUtils.remove(maleActors, actor);
        newRepo.femaleActors = SetUtils.remove(femaleActors, actor);
        newRepo.customHashes = customHashes;
        return newRepo;
    }
}
