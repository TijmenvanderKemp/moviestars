package com.tijmen;

import org.junit.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class HopcroftKarpGraphTest {

    @Test
    public void augmentingPathExists() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = new HopcroftKarpParser().parse(asStream);

        assertThat(graph.findAugmentingPath().isPresent());
    }
/*
    @Test
    public void augmentGraph() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = new HopcroftKarpParser().parse(asStream);
        LinkedList<Actor> augmentingPath1 = graph.findAugmentingPath().get();
        Actor firstActor = augmentingPath1.getLast();
        Set<Actor> firstActorOptions = new HashSet<>(graph.getCollabs().get(firstActor));
        graph.augmentGraph(augmentingPath1);
        LinkedList<Actor> augmentingPath2 = graph.findAugmentingPath().get();
        ActorSetAssert.assertThat(new HashSet<>(augmentingPath2)).isNotEqualTo(new HashSet<>(augmentingPath1));
        assertThat(graph.getCollabs().get(firstActor).size() < firstActorOptions.size());
    }*/
}