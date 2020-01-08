package com.tijmen.part1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ActorRepository {
    public Set<Integer> femaleActors = new HashSet<>();
    public Set<Integer> maleActors = new HashSet<>();
    public Map<String, Integer> customHashes = new HashMap<>();

    public Actor getByName(String name) {
        return new Actor(name, customHashes.get(name));
    }

    public Set<Integer> getAllActors() {
        Set<Integer> allActors = new HashSet<>(femaleActors);
        allActors.addAll(maleActors);
        return allActors;
    }

}
