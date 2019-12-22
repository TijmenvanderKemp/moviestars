package com.tijmen;

import java.util.*;

public class HopcroftKarpGraph {
    private Set<Actor> femaleActors; // left side
    private Set<Actor> maleActors; // right side
    private Set<Actor> freeWomen; // Women not part of the maximal matching
    private Set<Actor> freeMen; // Men not part of the maximal matching
    private Map<Actor, Map<Actor,Integer>> collabs; // edges

    public HopcroftKarpGraph(Set<Actor> femaleActors, Set<Actor> maleActors, Map<Actor, Map<Actor,Integer>> collabs) {
        this.femaleActors = femaleActors;
        this.maleActors = maleActors;
        this.collabs = collabs;
        freeMen = new HashSet<>(maleActors);
        freeWomen = new HashSet<>(femaleActors);
    }

    public Optional<LinkedList<Actor>> findAugmentingPath() {

        // breadth first search until a free man is encountered. return that free man if found, return empty optional otherwise.

        Queue<Actor> queue = new LinkedList<>(freeWomen);
        Map<Actor, Actor> predecessors = new HashMap<>();
        for (Actor woman : freeWomen) {
            predecessors.put(woman, null);
        }
        while (!queue.isEmpty()) {
            Actor actor = queue.remove();
            Map<Actor,Integer> coworkers = collabs.get(actor);
            for (Actor coworker : coworkers.keySet()) {
                if (freeMen.contains(coworker)) {
                    // create the augmenting path if a free man is found
                    predecessors.put(coworker, actor);
                    return Optional.of(createAugmentingPath(predecessors, coworker));
                }
                if (!predecessors.containsKey(coworker)) {
                    predecessors.put(coworker, actor);
                    queue.add(coworker);
                }
            }
        }
        return Optional.empty();
    }

    private LinkedList<Actor> createAugmentingPath(Map<Actor, Actor> predecessors, Actor finalChild) {
        LinkedList<Actor> augmentingPath = new LinkedList<>();
        Actor child = finalChild;
        while (child != null) {
            augmentingPath.add(child);
            child = predecessors.get(child);
        }
        return augmentingPath;
    }

    public void augmentGraph(LinkedList<Actor> augmentingPath) {

        freeWomen.remove(augmentingPath.getLast());
        freeMen.remove(augmentingPath.getFirst());

        Iterator<Actor> iterator = augmentingPath.descendingIterator();
        Actor actor1 = iterator.next();
        while (iterator.hasNext()) {
            Actor actor2 = iterator.next();
            switchDirections(actor1, actor2);
            actor1 = actor2;
        }

    }

    private void switchDirections(Actor a1, Actor a2) {
        if(collabs.get(a1).containsKey(a2)) {
            int number = collabs.get(a1).get(a2);
            collabs.get(a1).remove(a2);
            collabs.get(a2).put(a1, number);
        } else {
            int number = collabs.get(a1).get(a2);
            collabs.get(a2).remove(a1);
            collabs.get(a1).put(a2, number);
        }
    }

    public Set<Actor> getFreeWomen() {
        return freeWomen;
    }

    public Set<Actor> getFreeMen() {
        return freeMen;
    }

    public Set<Actor> getFemaleActors() {
        return femaleActors;
    }

    public Set<Actor> getMaleActors() {
        return maleActors;
    }

    public Map<Actor, Map<Actor, Integer>> getCollabs() {
        return collabs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HopcroftKarpGraph that = (HopcroftKarpGraph) o;
        return Objects.equals(femaleActors, that.femaleActors) &&
                Objects.equals(maleActors, that.maleActors) &&
                Objects.equals(freeWomen, that.freeWomen) &&
                Objects.equals(freeMen, that.freeMen) &&
                Objects.equals(collabs, that.collabs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(femaleActors, maleActors, freeWomen, freeMen, collabs);
    }
}
