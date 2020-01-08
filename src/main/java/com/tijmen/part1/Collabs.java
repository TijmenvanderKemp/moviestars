package com.tijmen.part1;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Bidirectional and mutable
public class Collabs {
    private final Map<Actor, Set<Actor>> collabs;

    public Collabs(Set<Actor> allActors) {
        collabs = allActors.stream()
                .collect(Collectors.toMap(actor -> actor, actor -> new HashSet<>()));
    }

    public Set<Actor> get(Actor actor) {
        return collabs.get(actor);
    }

    public void add(Actor actress, Actor actor) {
        collabs.get(actor).add(actress);
        collabs.get(actress).add(actor);
    }
}
