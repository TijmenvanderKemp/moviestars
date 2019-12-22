package com.tijmen;

public class Actor {
    public String name;
    public int hashCode;

    public Actor(String name, int customHash) {
        this.name = name;
        this.hashCode = customHash;
    }

    public String getName() {
        return name;
    }

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
