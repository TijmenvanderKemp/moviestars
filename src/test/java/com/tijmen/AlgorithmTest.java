package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class AlgorithmTest {

    @Test
    public void solve() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        Graph graph = new GraphParser().parse(asStream);
        Algorithm algorithm = new Algorithm(graph);
        assertThat(algorithm.solve()).isEqualTo(Player.MARK);
    }

    private Actor actor(String name) {
        return new Actor(name);
    }
}