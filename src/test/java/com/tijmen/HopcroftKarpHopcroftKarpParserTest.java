package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

public class HopcroftKarpHopcroftKarpParserTest {

    @Test
    public void test() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = new HopcroftKarpParser().parse(asStream);

        ActorSetAssert.assertThat(graph.getCollabs().get(actor("MelanieLaurent", 1)))
                .containsExactlyInAnyOrder("BradPitt");
        ActorSetAssert.assertThat(graph.getCollabs().get(actor("BradPitt", 2))).containsExactlyInAnyOrder();
        ActorSetAssert.assertThat(graph.getCollabs().get(actor("NormanReedus", 3)))
                .containsExactlyInAnyOrder();
        ActorSetAssert.assertThat(graph.getCollabs().get(actor("DianaKruger", 0)))
                .containsExactlyInAnyOrder("BradPitt", "NormanReedus");
    }

    private Actor actor(String name, int hash) {
        return new Actor(name, hash);
    }

}