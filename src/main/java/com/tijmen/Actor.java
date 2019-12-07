package com.tijmen;

import java.util.Objects;

public class Actor {
    public String name;
    public Sex sex;

    public Actor(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(name, actor.name) &&
                sex == actor.sex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sex);
    }
}
