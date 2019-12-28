package com.tijmen;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayingLineValueTest {
    @Test
    public void test() {
        assertThat(new PlayingLineValue().getScoreSoFar()).isEqualTo(0d);
        assertThat(new PlayingLineValue().getWeight()).isEqualTo(0);

        assertThat(new PlayingLineValue().add(5).getScoreSoFar()).isEqualTo(5d);
        assertThat(new PlayingLineValue().add(5).getWeight()).isEqualTo(1);

        assertThat(new PlayingLineValue().add(5).add(6).getScoreSoFar()).isEqualTo(5.5d);
        assertThat(new PlayingLineValue().add(5).add(6).getWeight()).isEqualTo(2);
    }

}