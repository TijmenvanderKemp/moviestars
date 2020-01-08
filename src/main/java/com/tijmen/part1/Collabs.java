package com.tijmen.part1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Bidirectional and mutable
public class Collabs {
    private final Map<Actor, Set<Actor>> collabs = new HashMap<>();
    private final boolean[][] collabsInit;
    private final int size;
    private final int pivot;

    public Collabs(int size) {
        this.size = size;
        pivot = size / 2;
        collabsInit = new boolean[size][size];
    }

    public Set<Actor> get(Actor actor) {
        return collabs.get(actor);
    }

    public void add(Actor actress, Actor actor) {
        collabsInit[actress.hashCode][actor.hashCode] = true;
        collabsInit[actor.hashCode][actress.hashCode] = true;
    }

    public void constructMap() {
        for (int j = 0; j < pivot; j++) {
            Set<Actor> actors = new HashSet<>();
            Set<Actor> actresses = new HashSet<>();
            for (int i = pivot; i < size; i++) {
                if (collabsInit[j][i]) {
                    actors.add(new Actor(null, i));
                }
                if (collabsInit[j + pivot][i - pivot]) {
                    actresses.add(new Actor(null, i - pivot));
                }
            }
            collabs.put(new Actor(null, j), actors);
            collabs.put(new Actor(null, j + pivot), actresses);
        }
    }
}
