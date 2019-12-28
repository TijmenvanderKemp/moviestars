package com.tijmen;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CompetitionRunnerTest {
    @Test
    public void testFlow() {
        OutputStub writer = new OutputStub();
        CompetitionRunner runner = new CompetitionRunnerStub(new InputStub(), writer);
        try {
            runner.run();
        } catch (WeWon weWon) {
            // Do nothing, the external program will have terminated our program
            assertThat(weWon.getFinalScore().getScoreSoFar()).isEqualTo(1d);
        }
        assertThat(writer.responses).containsExactly("BradPitt", "NormanReedus");
    }

    @Test
    public void testWithStrategies() {
        OutputStub writer = new OutputStub();
        CompetitionRunner runner = new CompetitionRunner(new InputStub(), writer);
        try {
            runner.run();
        } catch (WeWon weWon) {
            // Do nothing, the external program will have terminated our program
            assertThat(weWon.getFinalScore().getScoreSoFar()).isEqualTo(1d);
        }
        assertThat(writer.responses).containsExactly("BradPitt", "NormanReedus");
    }

    private static class CompetitionRunnerStub extends CompetitionRunner {

        public CompetitionRunnerStub(Reader reader, Writer writer) {
            super(reader, writer);
        }

        @Override
        Strategy createStrategy(Player victor) {
            return new FakeStrategy();
        }
    }

    private static class FakeStrategy implements Strategy {

        @Override
        public Pair<Actor, Score> nextMove(ProblemContext context) {
            switch (context.getTheirMove().name) {
                case "MelanieLaurent":
                    return new Pair<>(context.getProblem().actorRepository.getByName("BradPitt"), null);
                case "DianaKruger":
                    return new Pair<>(context.getProblem().actorRepository.getByName("NormanReedus"), null);
                default:
                    return null;
            }
        }
    }

    private static class InputStub implements Reader {
        List<String> moves = Arrays.asList("2 2",
                "DianaKruger",
                "MelanieLaurent",
                "BradPitt",
                "NormanReedus",
                "IngloriousBasterds",
                "3",
                "DianaKruger",
                "MelanieLaurent",
                "BradPitt",
                "Sky",
                "2",
                "DianaKruger",
                "NormanReedus",
                "Mark",
                "MelanieLaurent", "DianaKruger", "I give up");
        int counter = 0;

        @Override
        public String nextLine() {
            return moves.get(counter++);
        }
    }

    private static class AnyAdjacentActor implements Reader {
        @Override
        public String nextLine() {
            return null;
        }
    }

    private static class OutputStub implements Writer {
        private List<String> responses = new ArrayList<>();

        @Override
        public void println(String string) {
            responses.add(string);
        }
    }
}