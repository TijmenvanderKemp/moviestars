package com.tijmen;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AllFilesSimpleRepliesCompetitionTest {

    private String response;
    private Problem problem;


    @Test
    public void testAlleOfficieleFiles() {
        File inFolder = new File(TestFile.getAsUrl("samples/in/").getPath());
        File[] inFiles = Objects.requireNonNull(inFolder.listFiles());

        File outFolder = new File(TestFile.getAsUrl("samples/out/").getPath());
        File[] outFiles = Objects.requireNonNull(outFolder.listFiles());

        List<Pair<File, File>> insAndOuts = zip(inFiles, outFiles);

        for (Pair<File, File> inAndOut : insAndOuts) {
            test(inAndOut);
        }
    }

    private void test(Pair<File, File> inAndOut) {
        try {
            System.out.println("Testing " + inAndOut.getLeft().getName());
            HopcroftKarpGraph graph = getProblem(inAndOut);
            HopcroftKarpAlgorithm hopcroftKarpAlgorithm = new HopcroftKarpAlgorithm(graph);
            CompetitionRunner runner = new CompetitionRunner(new FileReader(inAndOut.getLeft(), graph.getCollabs()), new OutputStub());
            runner.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private HopcroftKarpGraph getProblem(Pair<File, File> inAndOut) throws FileNotFoundException {
        problem = new HopcroftKarpParser().parse(new FileInputStream(inAndOut.getLeft()));
        return HopcroftKarpGraph.of(problem);
    }

    private String getSolution(Pair<File, File> inAndOut) throws FileNotFoundException {
        return new Scanner(new FileInputStream(inAndOut.getRight())).nextLine();
    }

    public <A, B> List<Pair<A, B>> zip(A[] lefts, B[] rights) {
        if (lefts.length != rights.length) {
            throw new IllegalStateException("lijsten zijn niet even lang");
        }
        List<Pair<A, B>> pairs = new ArrayList<>();
        for (int i = 0; i < lefts.length; i++) {
            pairs.add(new Pair<>(lefts[i], rights[i]));
        }
        return pairs;
    }

    private class OutputStub implements Writer {

        @Override
        public void println(String string) {
            response = string;
        }
    }

    private class FileReader implements Reader {
        File fileName;
        Scanner scanner;
        private Map<Actor, Set<Actor>> collabs;
        boolean firstTime;

        public FileReader(File fileName, Map<Actor, Set<Actor>> collabs) {
            this.fileName = fileName;
            this.collabs = collabs;
            firstTime = true;
            try {
                scanner = new Scanner(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String nextLine() {
            if(scanner.hasNextLine()) {
                return scanner.nextLine();
            }
            if(firstTime) {
                firstTime = false;
                return "Veronique";
            }

            Set<Actor> options = collabs.get(new Actor(response, problem.actorRepository.customHashes.get(response)));
            if(options.isEmpty()) {
                return "I give up";
            }
            return options.iterator().next().name;
        }
    }
}
