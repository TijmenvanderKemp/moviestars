package com.tijmen;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Set;
import java.util.stream.Collectors;

public class ActorSetAssert extends AbstractAssert<ActorSetAssert, Set<Actor>> {

    public ActorSetAssert(Set<Actor> actual) {
        super(actual, ActorSetAssert.class);
    }

    public static ActorSetAssert assertThat(Set<Actor> actual) {
        return new ActorSetAssert(actual);
    }

    public void containsExactlyInAnyOrder(String... names) {
        Set<String> actorNames = actual.stream().map(Actor::getName).collect(Collectors.toSet());
        Assertions.assertThat(actorNames).containsExactlyInAnyOrder(names);
    }
}
