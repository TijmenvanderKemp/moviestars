package com.tijmen;

import java.util.*;
import java.util.stream.Collectors;

public class HopcroftKarpGraph {
    private final Set<Actor> femaleActors; // left side
    private final Set<Actor> maleActors; // right side
    private final Set<Actor> freeWomen; // Women not part of the maximal matching
    private final Set<Actor> freeMen; // Men not part of the maximal matching
    private final Collabs collabs; // bidirectional edges
    private boolean[][] matching;
    private Map<Actor, Actor> m2fMatching;
    private Map<Actor, Actor> f2mMatching;

    public HopcroftKarpGraph(Set<Actor> femaleActors, Set<Actor> maleActors, Collabs collabs) {
        this.femaleActors = femaleActors;
        this.maleActors = maleActors;
        this.collabs = collabs;
        int totalNumberOfActors = femaleActors.size()*2;
        matching = new boolean[totalNumberOfActors][totalNumberOfActors];
        freeMen = new HashSet<>(maleActors);
        freeWomen = new HashSet<>(femaleActors);
    }


    public static HopcroftKarpGraph of(Problem problem) {
        return new HopcroftKarpGraph(problem.actorRepository.femaleActors, problem.actorRepository.maleActors, problem.collabs);
    }

    public Set<LinkedList<Actor>> findAugmentingPaths() {

        // breadth first search until
        Queue<Actor> queue = new LinkedList<>(freeWomen);
        Map<Actor, Actor> predecessors = new HashMap<>();
        Map<Actor, Integer> partitioning = new HashMap<>();
        for (Actor woman : freeWomen) {
            partitioning.put(woman, 1);
        }
        int partitionCounter = 2;
        boolean hasToBeMatching = false;
        while (!queue.isEmpty()) {
            Actor actor = queue.remove();
            Set<Actor> coworkers = collabs.get(actor);
            for (Actor coworker : coworkers) {
                if (!partitioning.containsKey(coworker) && hasToBeMatching == matching[actor.hashCode][coworker.hashCode]) {
                    queue.add(coworker);
                    partitioning.put(coworker, partitionCounter);
                }
            }
            hasToBeMatching = !hasToBeMatching;
            partitionCounter++;
        }

        //depth first search
        HashSet<Actor> encounteredActors = new HashSet<>(freeWomen);

        Set<LinkedList<Actor>> augmentingPaths = new HashSet<>();
        for(Actor actor : freeWomen) {
            LinkedList<Actor> augmentingPath = dfs(actor, partitioning, encounteredActors);
            if (augmentingPath != null) {
                augmentingPaths.add(augmentingPath);
            }
        }

        return augmentingPaths;
    }

    private LinkedList<Actor> dfs(Actor currentActor, Map<Actor, Integer> partitioning, HashSet<Actor> encounteredActors) {
        Stack<Pair<Actor, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(currentActor, 1));
        Map<Actor, Actor> predecessors = new HashMap<>();
        while(!stack.empty()) {
            Pair<Actor, Integer> actorAndDepth = stack.pop();
            Actor actor = actorAndDepth.getLeft();
            int depth = actorAndDepth.getRight();
            Set<Actor> coworkers = collabs.get(actor);
            for(Actor coworker : coworkers) {
                if(partitioning.get(coworker) == depth + 1 && !encounteredActors.contains(coworker)) {
                    encounteredActors.add(coworker);
                    predecessors.put(coworker, actor);
                    if(freeMen.contains(coworker)) {
                        return createAugmentingPath(predecessors, coworker);
                    }
                    stack.push(new Pair<>(coworker, depth + 1));

                }
            }
        }

        return null;
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

    public  void augmentGraph(Set<LinkedList<Actor>> augmentingPaths) {
        for(LinkedList<Actor> augmentingPath : augmentingPaths) {
            augmentGraph(augmentingPath);
        }
    }

    public void augmentGraph(LinkedList<Actor> augmentingPath) {
        freeWomen.remove(augmentingPath.getLast());
        freeMen.remove(augmentingPath.getFirst());

        Iterator<Actor> iterator = augmentingPath.descendingIterator();
        Actor actor1 = iterator.next();
        while (iterator.hasNext()) {
            Actor actor2 = iterator.next();
            switchMatching(actor1, actor2);
            actor1 = actor2;
        }
    }

    private void switchMatching(Actor a1, Actor a2) {
        matching[a1.hashCode][a2.hashCode] = !matching[a1.hashCode][a2.hashCode];
        matching[a2.hashCode][a1.hashCode] = !matching[a2.hashCode][a1.hashCode];
    }

    public Set<Actor> getFreeWomen() {
        return freeWomen;
    }

    public Collabs getCollabs() {
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
        for(Actor actress : femaleActors) {
            for(Actor actor : maleActors) {
                if( matching[actress.hashCode][actor.hashCode] ) {
                    f2mMatching.put(actress, actor);
                    m2fMatching.put(actor, actress);
                }
            }
        }
    }
}
