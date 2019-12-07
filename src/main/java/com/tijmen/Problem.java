package com.tijmen;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Problem {
    private Player toMove;
    private Set<Actor> moveOptions;
    private Map<Actor, Set<Actor>> collabs;

    public Player getToMove() {
        return toMove;
    }

    public Set<Actor> getMoveOptions() {
        return moveOptions;
    }

    public Map<Actor, Set<Actor>> getCollabs() {
        return collabs;
    }

    public Problem(Player toMove, Set<Actor> moveOptions, Map<Actor, Set<Actor>> collabs) {
        this.toMove = toMove;
        this.moveOptions = moveOptions;
        this.collabs = collabs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return toMove == problem.toMove &&
                Objects.equals(moveOptions, problem.moveOptions) &&
        Objects.equals(collabs, problem.collabs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toMove, moveOptions, collabs);
    }
}
