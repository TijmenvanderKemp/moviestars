package com.tijmen.part2;

import org.junit.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

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
        List<Actor> fs = Stream.of(new Actor("A", 0), new Actor("B", 1)).collect(Collectors.toList());
        List<Actor> ms = Stream.of(new Actor("C", 2), new Actor("D", 3)).collect(Collectors.toList());
        Collabs collabs = new Collabs(Stream.concat(fs.stream(), ms.stream()).collect(Collectors.toSet()));
        collabs.add(fs.get(0), ms.get(0));
        collabs.add(fs.get(0), ms.get(1));
        collabs.add(fs.get(1), ms.get(1));

        HopcroftKarpGraph graph = new HopcroftKarpGraph(new HashSet<>(fs), new HashSet<>(ms), collabs);
        Map<Actor, Integer> partitioning = graph.partition();
        assertThat(partitioning).containsExactly(
                entry(fs.get(0), 1),
                entry(fs.get(1), 1),
                entry(ms.get(0), 2),
                entry(ms.get(1), 2)
        );
    }
}