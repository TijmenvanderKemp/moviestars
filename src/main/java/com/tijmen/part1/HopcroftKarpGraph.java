package com.tijmen.part1;

import java.util.*;
import java.util.stream.Collectors;

public class HopcroftKarpGraph {
    private final Set<Integer> femaleActors; // left side
    private final Set<Integer> maleActors; // right side
    private final Set<Integer> freeWomen; // Women not part of the maximal matching
    private final Set<Integer> freeMen; // Men not part of the maximal matching
    private final Collabs collabs; // bidirectional edges
    private boolean[][] matching;

    public HopcroftKarpGraph(Set<Integer> femaleActors, Set<Integer> maleActors, Collabs collabs) {
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

    public Set<List<Integer>> findAugmentingPaths() {
        Map<Integer, Integer> partitioning = partition();

        //depth first search
        HashSet<Integer> encounteredActors = new HashSet<>(freeWomen);

        Set<List<Integer>> augmentingPaths = new HashSet<>();
        for (Integer actor : freeWomen) {
            List<Integer> augmentingPath = dfs(actor, partitioning, encounteredActors);
            if (augmentingPath != null) {
                augmentingPaths.add(augmentingPath);
            }
        }

        return augmentingPaths;
    }

    Map<Integer, Integer> partition() {
        // breadth first search until
        Queue<Pair<Integer, Integer>> queue = freeWomen.stream().map(actress -> new Pair<>(actress, 1)).collect(Collectors.toCollection(LinkedList::new));
        Map<Integer, Integer> partitioning = new HashMap<>();
        for (Integer woman : freeWomen) {
            partitioning.put(woman, 1);
        }
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> actorAndDepth = queue.remove();
            Integer actor = actorAndDepth.getLeft();
            Set<Integer> coworkers = collabs.get(actor);
            int depth = actorAndDepth.getRight();
            boolean hasToBeMatching = depth % 2 == 0;
            for (Integer coworker : coworkers) {
                if (!partitioning.containsKey(coworker) && hasToBeMatching == matching[actor][coworker]) {
                    queue.add(new Pair<>(coworker, depth + 1));
                    partitioning.put(coworker, depth + 1);
                }
            }
        }
        return partitioning;
    }

    private List<Integer> dfs(Integer currentActor, Map<Integer, Integer> partitioning, HashSet<Integer> encounteredActors) {
        Stack<Pair<Integer, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(currentActor, 1));
        Map<Integer, Integer> predecessors = new HashMap<>();
        while (!stack.empty()) {
            Pair<Integer, Integer> actorAndDepth = stack.pop();
            Integer actor = actorAndDepth.getLeft();
            int depth = actorAndDepth.getRight();
            Set<Integer> coworkers = collabs.get(actor);
            for (Integer coworker : coworkers) {
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

    private List<Integer> createAugmentingPath(Map<Integer, Integer> predecessors, Integer finalChild) {
        List<Integer> augmentingPath = new ArrayList<>();
        Integer child = finalChild;
        while (child != null) {
            augmentingPath.add(child);
            child = predecessors.get(child);
        }
        return augmentingPath;
    }

    public void augmentGraph(Set<List<Integer>> augmentingPaths) {
        for (List<Integer> augmentingPath : augmentingPaths) {
            augmentGraph(augmentingPath);
        }
    }

    public void augmentGraph(List<Integer> augmentingPath) {
        Iterator<Integer> iterator = augmentingPath.iterator();
        Integer actor1 = iterator.next();
        freeMen.remove(actor1);

        Integer actor2 = null;
        while (iterator.hasNext()) {
            actor2 = iterator.next();
            switchMatching(actor1, actor2);
            actor1 = actor2;
        }
        freeWomen.remove(actor2);
    }

    private void switchMatching(Integer a1, Integer a2) {
        matching[a1][a2] = !matching[a1][a2];
        matching[a2][a1] = !matching[a2][a1];
    }

    public Set<Integer> getFreeWomen() {
        return freeWomen;
    }

    public Collabs getCollabs() {
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

