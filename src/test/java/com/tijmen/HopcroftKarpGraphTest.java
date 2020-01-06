package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class HopcroftKarpGraphTest {

    @Test
    public void augmentingPathExists() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = HopcroftKarpGraph.of(new HopcroftKarpParser().parse(asStream));

        assertThat(graph.findAugmentingPaths()).isNotEmpty();
    }

    @Test
    public void augmentGraph() {
        /*
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = HopcroftKarpGraph.of(new HopcroftKarpParser().parse(asStream));
        LinkedList<Actor> augmentingPath1 = graph.findAugmentingPaths().get();
        Actor firstActor = augmentingPath1.getLast();
        Set<Actor> firstActorOptions = new HashSet<>(graph.getCollabs().get(firstActor));
        graph.augmentGraph(augmentingPath1);
        LinkedList<Actor> augmentingPath2 = graph.findAugmentingPaths().get();
        ActorSetAssert.assertThat(new HashSet<>(augmentingPath2)).isNotEqualTo(new HashSet<>(augmentingPath1));
        assertThat(graph.getCollabs().get(firstActor).size() < firstActorOptions.size()).isTrue();
         */
    }

    @Test
    public void testPartitioning() {
//        Set<Actor> fs = Stream.of(new Actor("A", 1), new Actor("B", 2)).collect(Collectors.toSet());
//        Set<Actor> ms = Stream.of(new Actor("A", 1), new Actor("B", 2)).collect(Collectors.toSet());
//
//        new HopcroftKarpGraph()
    }
}