package com.tijmen;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

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
            response = null;
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
        } catch (FinalGameState e) {
            System.out.println(e.getClass());
            System.out.println(e.getFinalScore().getScoreSoFar());
            System.out.println(new FileReader(inAndOut.getRight(), null).nextLine());
            System.out.println("=============");
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
            System.out.println(string);
        }
    }

    private class FileReader implements Reader {
        File fileName;
        Scanner scanner;
        private Map<Actor, Set<Actor>> collabs;
        boolean firstTime;
        Set<Actor> saidActors;

        public FileReader(File fileName, Map<Actor, Set<Actor>> collabs) {
            this.fileName = fileName;
            this.collabs = collabs;
            firstTime = true;
            saidActors = new HashSet<>();
            try {
                scanner = new Scanner(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String nextLine() {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
            if (firstTime) {
                firstTime = false;
                return "Mark";
            }
            if(response == null) {
                return problem.actorRepository.femaleActors.iterator().next().name;
            }

            Actor responseActor = new Actor(response, problem.actorRepository.customHashes.get(response));
            Optional<Actor> option = problem.collabCount.get(responseActor).keySet()
                    .stream()
                    .filter(actor -> !saidActors.contains(actor))
                    .findFirst();
            if (!option.isPresent()) {
                System.out.println("I give up");
                return "I give up";
            }
            saidActors.add(option.get());

            System.out.println(option.get().name);
            return option.get().name;
        }
    }
}
