package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class HopcroftKarpAlgorithmTest {

    @Test
    public void solve() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = HopcroftKarpGraph.of(new HopcroftKarpParser().parse(asStream));
        HopcroftKarpAlgorithm hopcroftKarpAlgorithm = new HopcroftKarpAlgorithm(graph);
        assertThat(hopcroftKarpAlgorithm.solve().getLeft()).isEqualTo(Player.MARK);
    }

}