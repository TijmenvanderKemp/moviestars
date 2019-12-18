package com.tijmen;

import java.util.*;

public class Graph {
    private Set<Actor> femaleActors; // left side
    private Set<Actor> maleActors; // right side
    private Set<Actor> freeWomen; // Women not part of the maximal matching
    private Set<Actor> freeMen; // Men not part of the maximal matching
    private Map<Actor, Set<Actor>> collabs; // edges

    public Graph(Set<Actor> femaleActors, Set<Actor> maleActors, Map<Actor, Set<Actor>> collabs) {
        this.femaleActors = femaleActors;
        this.maleActors = maleActors;
        this.collabs = collabs;
        freeMen = new HashSet<>(maleActors);
        freeWomen = new HashSet<>(femaleActors);
    }

    public Optional<LinkedList<Actor>> augmentingPathExists() {

        // breadth first search until a free man is encountered. return that free man if found, return empty optional otherwise.

        Queue<Actor> queue = new LinkedList(freeWomen);
        Map<Actor, Actor> parent = new HashMap<>();
        for( Actor woman : freeWomen) {
            parent.put(woman, null);
        }
        while( !queue.isEmpty() ) {
            Actor actor = queue.remove();
            Set<Actor> coworkers = collabs.get(actor);
            for(Actor coworker : coworkers) {
                if(freeMen.contains(coworker)) {
                    // create the augmenting path if a free man is found
                    parent.put(coworker, actor);
                    LinkedList<Actor> augmentingPath = new LinkedList<>();
                    Actor child = coworker;
                    while(child != null) {
                        augmentingPath.add(child);
                        child = parent.get(child);
                    }
                    return Optional.of(augmentingPath);
                }
                if(!parent.containsKey(coworker)) {
                    parent.put(coworker, actor);
                    queue.add(coworker);
                }
            }
        }
        return Optional.empty();
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
        if(collabs.get(a1).contains(a2)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph that = (Graph) o;
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
