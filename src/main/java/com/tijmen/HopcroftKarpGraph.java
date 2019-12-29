package com.tijmen;

import java.util.*;
import java.util.stream.Collectors;

public class HopcroftKarpGraph {
    private Set<Actor> femaleActors; // left side
    private Set<Actor> maleActors; // right side
    private Set<Actor> freeWomen; // Women not part of the maximal matching
    private Set<Actor> freeMen; // Men not part of the maximal matching
    private Map<Actor, Set<Actor>> collabs; // edges
    private Map<Actor, Actor> m2fMatching;
    private Map<Actor, Actor> f2mMatching;

    public HopcroftKarpGraph(Set<Actor> femaleActors, Set<Actor> maleActors, Map<Actor, Set<Actor>> collabs) {
        this.femaleActors = femaleActors;
        this.maleActors = maleActors;
        this.collabs = new HashMap<>(collabs);
        freeMen = new HashSet<>(maleActors);
        freeWomen = new HashSet<>(femaleActors);
    }


    public static HopcroftKarpGraph of(Problem problem) {
        return new HopcroftKarpGraph(problem.actorRepository.femaleActors, problem.actorRepository.maleActors, problem.hopcroftKarpCollabs);
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
            Set<Actor> coworkers = collabs.get(actor);
            for (Actor coworker : coworkers) {
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
        if (collabs.get(a1).contains(a2)) {
            collabs.get(a1).remove(a2);
            collabs.get(a2).add(a1);
        } else {
            collabs.get(a2).remove(a1);
            collabs.get(a1).add(a2);
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

    public Map<Actor, Set<Actor>> getCollabs() {
        return collabs;
    }

    public Map<Actor, Actor> getM2fMatching() {
        return m2fMatching;
    }

    public Map<Actor, Actor> getF2mMatching() {
        return f2mMatching;
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

    public void calculateMatching() {
        Set<Actor> occupiedMen = new HashSet<>(maleActors);
        occupiedMen.removeAll(freeMen);
        m2fMatching = occupiedMen.stream().collect(Collectors.toMap(
                occupiedMan -> occupiedMan, occupiedMan -> collabs.get(occupiedMan).iterator().next()));

        f2mMatching = m2fMatching.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
