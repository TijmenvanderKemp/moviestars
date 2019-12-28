package com.tijmen;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScoreTest {
    @Test
    public void test() {
        assertThat(new Score().getScoreSoFar()).isEqualTo(0d);
        assertThat(new Score().getWeight()).isEqualTo(0);

        assertThat(new Score().add(5).getScoreSoFar()).isEqualTo(5d);
        assertThat(new Score().add(5).getWeight()).isEqualTo(1);

        assertThat(new Score().add(5).add(6).getScoreSoFar()).isEqualTo(5.5d);
        assertThat(new Score().add(5).add(6).getWeight()).isEqualTo(2);
    }

}