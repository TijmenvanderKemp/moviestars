package com.tijmen;

import com.tijmen.hopcroftkarp.HopcroftKarpAlgorithm;
import com.tijmen.hopcroftkarp.HopcroftKarpGraph;
import com.tijmen.hopcroftkarp.HopcroftKarpParser;
import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class HopcroftKarpAlgorithmTest {

    @Test
    public void solve() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = new HopcroftKarpParser().parse(asStream);
        HopcroftKarpAlgorithm hopcroftKarpAlgorithm = new HopcroftKarpAlgorithm(graph);
        assertThat(hopcroftKarpAlgorithm.solve()).isEqualTo(Player.MARK);
    }

    private Actor actor(String name) {
        return new Actor(name);
    }
}