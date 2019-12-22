package com.tijmen.minmax;

import com.tijmen.Actor;

import java.util.Objects;
import java.util.Set;

public class SubProblem {
    private Set<Actor> picked;
    private Actor actor;

    public SubProblem(Set<Actor> picked, Actor actor) {
        this.picked = picked;
        this.actor = actor;
    }

    public Set<Actor> getPicked() {
        return picked;
    }

    public Actor getActor() {
        return actor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubProblem that = (SubProblem) o;
        return picked.equals(that.picked) &&
                actor.equals(that.actor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(picked, actor);
    }
}
