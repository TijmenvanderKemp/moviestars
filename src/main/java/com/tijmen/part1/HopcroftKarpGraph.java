package com.tijmen.part1;

import java.util.*;

public class HopcroftKarpGraph {
    private final Set<Integer> femaleActors; // left side
    private final Set<Integer> maleActors; // right side
    private final Set<Integer> freeWomen; // Women not part of the maximal matching
    private final Set<Integer> freeMen; // Men not part of the maximal matching
    private final Collabs collabs; // bidirectional edges
    private int totalNumberOfActors;
    private boolean[][] matching;

    public HopcroftKarpGraph(Set<Integer> femaleActors, Set<Integer> maleActors, Collabs collabs) {
        this.femaleActors = femaleActors;
        this.maleActors = maleActors;
        this.collabs = collabs;
        totalNumberOfActors = femaleActors.size() * 2;
        matching = new boolean[totalNumberOfActors][totalNumberOfActors];
        freeMen = new HashSet<>(maleActors);
        freeWomen = new HashSet<>(femaleActors);
    }


    public static HopcroftKarpGraph of(Problem problem) {
        return new HopcroftKarpGraph(problem.actorRepository.femaleActors, problem.actorRepository.maleActors, problem.collabs);
    }

    public Set<List<Integer>> findAugmentingPaths() {
        //breadth first search
        int[] partitioning = partition();

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

    int[] partition() {
        // breadth first search until
        Queue<Integer> queue = new ArrayDeque<>(freeWomen);
        int[] partitioning = new int[totalNumberOfActors];
        for (Integer woman : freeWomen) {
            partitioning[woman] = 1;
        }
        while (!queue.isEmpty()) {
            Integer actor = queue.remove();
            Set<Integer> coworkers = collabs.get(actor);
            int depth = partitioning[actor];
            boolean hasToBeMatching = depth % 2 == 0;
            for (Integer coworker : coworkers) {
                if (partitioning[coworker] == 0 && hasToBeMatching == matching[actor][coworker]) {
                    queue.add(coworker);
                    partitioning[coworker] = depth + 1;
                }
            }
        }
        return partitioning;
    }

    private List<Integer> dfs(Integer currentActor, int[] partitioning, HashSet<Integer> encounteredActors) {
        Stack<Integer> stack = new Stack<>();
        stack.push(currentActor);
        int[] predecessors = new int[totalNumberOfActors];
        for(int i = 0; i < totalNumberOfActors; i++) {
            predecessors[i] = -1;
        }
        while (!stack.isEmpty()) {
            Integer actor = stack.pop();
            int depth = partitioning[actor];
            Set<Integer> coworkers = collabs.get(actor);
            for (Integer coworker : coworkers) {
                if (!encounteredActors.contains(coworker) && partitioning[coworker] == depth + 1) {
                    encounteredActors.add(coworker);
                    predecessors[coworker] = actor;
                    if (freeMen.contains(coworker)) {
                        return createAugmentingPath(predecessors, coworker);
                    }
                    stack.push(coworker);
                }
            }
        }

        return null;
    }

    private List<Integer> createAugmentingPath(int[] predecessors, Integer finalChild) {
        List<Integer> augmentingPath = new ArrayList<>();
        int child = finalChild;
        while (child != -1) {
            augmentingPath.add(child);
            child = predecessors[child];
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

