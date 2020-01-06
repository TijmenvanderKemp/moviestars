package com.tijmen;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

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
            problem = new HopcroftKarpParser().parse(new FileInputStream(inAndOut.getLeft()));
            CompetitionRunner runner = new CompetitionRunner(new FileReader(inAndOut.getLeft()), new OutputStub());
            runner.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FinalGameState e) {
            System.out.println(e.getClass());
            System.out.println(e.getFinalScore().getScoreSoFar());
            System.out.println(new FileReader(inAndOut.getRight()).nextLine());
            System.out.println("=============");
        }
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
//            System.out.println(string);
        }
    }

    private class FileReader implements Reader {
        final File fileName;
        Scanner scanner;
        boolean firstTime;
        final Set<Actor> saidActors;

        public FileReader(File fileName) {
            this.fileName = fileName;
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
                Actor actor = problem.actorRepository.femaleActors.iterator().next();
                saidActors.add(actor);
                return actor.name;
            }

            Actor responseActor = problem.actorRepository.getByName(response);
            Optional<Actor> option = problem.collabCount.get(responseActor).keySet()
                    .stream()
                    .filter(actor -> !saidActors.contains(actor))
                    .findFirst();
            if (!option.isPresent()) {
                System.out.println("I give up");
                return "I give up";
            }
            saidActors.add(option.get());

            return option.get().name;
        }
    }
}
