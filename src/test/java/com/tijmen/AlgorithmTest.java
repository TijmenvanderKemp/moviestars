package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class AlgorithmTest {

    @Test
    public void solve() {
        InputStream asStream = TestFile.getAsStream("samples/a1.in");
        Problem problem = new ProblemParser().parse(asStream);
        Algorithm algorithm = new Algorithm(problem);
        assertThat(algorithm.solve()).isEqualTo(Player.MARK);
    }
}