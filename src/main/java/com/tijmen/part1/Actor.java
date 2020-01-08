package com.tijmen.part1;

public class Actor {
    public final String name;
    public final int hashCode;

    public Actor(String name, int customHash) {
        this.name = name;
        this.hashCode = customHash;
    }

    public String getName() {
        return name;
    }

    /**
     * We assume that we always call this with another Actor. We think it gives better performance not to check the type
     * and just let it blow up when we make the wrong move.
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Actor actor = (Actor) o;
        return hashCode == actor.hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return name;
    }
}
