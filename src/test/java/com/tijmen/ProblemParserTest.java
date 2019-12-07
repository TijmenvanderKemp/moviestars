package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

import static com.tijmen.Player.VERONIQUE;
import static org.assertj.core.api.Assertions.assertThat;

public class ProblemParserTest {

    @Test
    public void test() {
        InputStream asStream = TestFile.getAsStream("samples/a1.in");
        Problem problem = new ProblemParser().parse(asStream);

        assertThat(problem.getToMove()).isEqualTo(VERONIQUE);
    }

}