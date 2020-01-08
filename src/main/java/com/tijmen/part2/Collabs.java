package com.tijmen.part2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Bidirectional and mutable
public class Collabs {
    private final Map<Actor, Set<Actor>> collabs;
    private final Set<Actor> ignored = new HashSet<>();

    public Collabs(Set<Actor> allActors) {
        collabs = allActors.stream()
                .collect(Collectors.toMap(actor -> actor, actor -> new HashSet<>()));
    }

    public Set<Actor> get(Actor actor) {
        if (ignored.contains(actor)) {
            return Collections.emptySet();
        }
        return collabs.get(actor).stream()
                .filter(it -> !ignored.contains(it))
                .collect(Collectors.toSet());
    }

    public void ignore(Actor option) {
        ignored.add(option);
    }

    public void acknowledge(Actor option) {
        ignored.remove(option);
    }

    public void add(Actor actress, Actor actor) {
        collabs.get(actor).add(actress);
        collabs.get(actress).add(actor);
    }
}
