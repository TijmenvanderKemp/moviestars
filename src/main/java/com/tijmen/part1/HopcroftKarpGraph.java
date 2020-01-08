package com.tijmen.part1;

import java.util.*;
import java.util.stream.Collectors;

public class HopcroftKarpGraph {
    private final Set<Actor> femaleActors; // left side
    private final Set<Actor> maleActors; // right side
    private final Set<Actor> freeWomen; // Women not part of the maximal matching
    private final Set<Actor> freeMen; // Men not part of the maximal matching
    private final Collabs collabs; // bidirectional edges
    private boolean[][] matching;
    private Map<Actor, Actor> m2fMatching = new HashMap<>();
    private Map<Actor, Actor> f2mMatching = new HashMap<>();

    public HopcroftKarpGraph(Set<Actor> femaleActors, Set<Actor> maleActors, Collabs collabs) {
        this.femaleActors = femaleActors;
        this.maleActors = maleActors;
        this.collabs = collabs;
        int totalNumberOfActors = femaleActors.size() * 2;
        matching = new boolean[totalNumberOfActors][totalNumberOfActors];
        freeMen = new HashSet<>(maleActors);
        freeWomen = new HashSet<>(femaleActors);
    }


    public static HopcroftKarpGraph of(Problem problem) {
        return new HopcroftKarpGraph(problem.actorRepository.femaleActors, problem.actorRepository.maleActors, problem.collabs);
    }

    public Set<LinkedList<Actor>> findAugmentingPaths() {
        Map<Actor, Integer> partitioning = partition();

        //depth first search
        HashSet<Actor> encounteredActors = new HashSet<>(freeWomen);

        Set<LinkedList<Actor>> augmentingPaths = new HashSet<>();
        for (Actor actor : freeWomen) {
            LinkedList<Actor> augmentingPath = dfs(actor, partitioning, encounteredActors);
            if (augmentingPath != null) {
                augmentingPaths.add(augmentingPath);
            }
        }

        return augmentingPaths;
    }

    Map<Actor, Integer> partition() {
        // breadth first search until
        Queue<Pair<Actor, Integer>> queue = freeWomen.stream().map(actress -> new Pair<>(actress, 1)).collect(Collectors.toCollection(LinkedList::new));
        Map<Actor, Integer> partitioning = new HashMap<>();
        for (Actor woman : freeWomen) {
            partitioning.put(woman, 1);
        }
        while (!queue.isEmpty()) {
            Pair<Actor, Integer> actorAndDepth = queue.remove();
            Actor actor = actorAndDepth.getLeft();
            Set<Actor> coworkers = collabs.get(actor);
            int depth = actorAndDepth.getRight();
            boolean hasToBeMatching = depth % 2 == 0;
            for (Actor coworker : coworkers) {
                if (!partitioning.containsKey(coworker) && hasToBeMatching == matching[actor.hashCode][coworker.hashCode]) {
                    queue.add(new Pair<>(coworker, depth + 1));
                    partitioning.put(coworker, depth + 1);
                }
            }
        }
        return partitioning;
    }

    private LinkedList<Actor> dfs(Actor currentActor, Map<Actor, Integer> partitioning, HashSet<Actor> encounteredActors) {
        Stack<Pair<Actor, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(currentActor, 1));
        Map<Actor, Actor> predecessors = new HashMap<>();
        while (!stack.empty()) {
            Pair<Actor, Integer> actorAndDepth = stack.pop();
            Actor actor = actorAndDepth.getLeft();
            int depth = actorAndDepth.getRight();
            Set<Actor> coworkers = collabs.get(actor);
            for (Actor coworker : coworkers) {
                if (!encounteredActors.contains(coworker) && partitioning.containsKey(coworker) && partitioning.get(coworker) == depth + 1) {
                    encounteredActors.add(coworker);
                    predecessors.put(coworker, actor);
                    if (freeMen.contains(coworker)) {
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

    public void augmentGraph(Set<LinkedList<Actor>> augmentingPaths) {
        for (LinkedList<Actor> augmentingPath : augmentingPaths) {
            augmentGraph(augmentingPath);
        }
    }

    public void augmentGraph(LinkedList<Actor> augmentingPath) {
        Iterator<Actor> iterator = augmentingPath.iterator();
        Actor actor1 = iterator.next();
        freeMen.remove(actor1);

        Actor actor2 = null;
        while (iterator.hasNext()) {
            actor2 = iterator.next();
            switchMatching(actor1, actor2);
            actor1 = actor2;
        }
        freeWomen.remove(actor2);
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
        for (Actor actress : femaleActors) {
            for (Actor actor : maleActors) {
                if (matching[actress.hashCode][actor.hashCode]) {
                    f2mMatching.put(actress, actor);
                    m2fMatching.put(actor, actress);
                }
            }
        }
    }
}

