package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

import static com.tijmen.Player.VERONIQUE;
import static org.assertj.core.api.Assertions.assertThat;

public class ProblemParserTest {

    @Test
    public void test() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        Problem problem = new ProblemParser().parse(asStream);

        assertThat(problem.getToMove()).isEqualTo(VERONIQUE);
        ActorSetAssert.assertThat(problem.getMoveOptions())
                .containsExactlyInAnyOrder("MelanieLaurent", "DianaKruger");
        ActorSetAssert.assertThat(problem.getCollabs().get(actor("MelanieLaurent")))
                .containsExactlyInAnyOrder("BradPitt");
        ActorSetAssert.assertThat(problem.getCollabs().get(actor("BradPitt")))
                .containsExactlyInAnyOrder("MelanieLaurent", "DianaKruger");
        ActorSetAssert.assertThat(problem.getCollabs().get(actor("NormanReedus")))
                .containsExactlyInAnyOrder("DianaKruger");
        ActorSetAssert.assertThat(problem.getCollabs().get(actor("DianaKruger")))
                .containsExactlyInAnyOrder("BradPitt", "NormanReedus");
    }

    private Actor actor(String name) {
        return new Actor(name);
    }

}