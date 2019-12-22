package com.tijmen;

import org.junit.Test;

import java.io.InputStream;

public class HopcroftKarpHopcroftKarpParserTest {

    @Test
    public void test() {
        InputStream asStream = TestFile.getAsStream("samples/in/a1.in");
        HopcroftKarpGraph graph = new HopcroftKarpParser().parse(asStream);

        ActorSetAssert.assertThat(graph.getCollabs().get(actor("MelanieLaurent")))
                .containsExactlyInAnyOrder("BradPitt");
        ActorSetAssert.assertThat(graph.getCollabs().get(actor("BradPitt"))).containsExactlyInAnyOrder();
        ActorSetAssert.assertThat(graph.getCollabs().get(actor("NormanReedus")))
                .containsExactlyInAnyOrder();
        ActorSetAssert.assertThat(graph.getCollabs().get(actor("DianaKruger")))
                .containsExactlyInAnyOrder("BradPitt", "NormanReedus");
    }

    private Actor actor(String name) {
        return new Actor(name);
    }

}