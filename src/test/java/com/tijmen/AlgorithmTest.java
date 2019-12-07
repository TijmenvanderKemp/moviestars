package com.tijmen;

import org.junit.Test;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class AlgorithmTest {

    @Test
    public void testDeepCopyMapModifySource() {
        Map<Actor, Set<Actor>> source = Map.of(actor("A"),
                Stream.of(actor("B"), actor("C")).collect(Collectors.toSet()));
        Map<Actor, Set<Actor>> copy = Algorithm.deepCopyMap(source);
        source.get(actor("A")).remove(actor("B"));
        assertThat(copy.values()).containsExactly(Set.of(actor("B"), actor("C")));
    }

    @Test
    public void testDeepCopyMapModifyCopy() {
        Map<Actor, Set<Actor>> source = Map.of(actor("A"),
                Stream.of(actor("B"), actor("C")).collect(Collectors.toSet()));
        Map<Actor, Set<Actor>> copy = Algorithm.deepCopyMap(source);
        copy.get(actor("A")).remove(actor("B"));
        assertThat(source.values()).containsExactly(Set.of(actor("B"), actor("C")));
    }

    @Test
    public void solve() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        Problem problem = new ProblemParser().parse(asStream);
        Algorithm algorithm = new Algorithm(problem);
        assertThat(algorithm.solve()).isEqualTo(Player.MARK);
    }

    private Actor actor(String name) {
        return new Actor(name);
    }
}