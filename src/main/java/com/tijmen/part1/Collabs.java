package com.tijmen.part1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Bidirectional and mutable
public class Collabs {
    private final Map<Integer, Set<Integer>> collabs = new HashMap<>();
    private final boolean[][] collabsInit;
    private final int size;
    private final int pivot;

    public Collabs(int size) {
        this.size = size;
        pivot = size / 2;
        collabsInit = new boolean[size][size];
    }

    public Set<Integer> get(Integer actor) {
        return collabs.get(actor);
    }

    public void add(int actress, int actor) {
        collabsInit[actress][actor] = true;
        collabsInit[actor][actress] = true;
    }

    public void constructMap() {
        for (int j = 0; j < pivot; j++) {
            Set<Integer> actors = new HashSet<>();
            Set<Integer> actresses = new HashSet<>();
            for (int i = pivot; i < size; i++) {
                if (collabsInit[j][i]) {
                    actors.add(i);
                }
                if (collabsInit[j + pivot][i - pivot]) {
                    actresses.add(i - pivot);
                }
            }
            collabs.put(j, actors);
            collabs.put(j + pivot, actresses);
        }
    }
}
