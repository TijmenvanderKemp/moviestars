package com.tijmen;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class AllFilesAgainstEachOther {

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
            test(inAndOut.getLeft(), inAndOut.getRight());
            response = null;
        }
    }


    private void test(File in, File out) {
        System.out.println("=====================");
        System.out.println("Testing " + in.getName());
        String solution = null;
        solution = getSolution(out);
        try {
            new Judge(in, solution);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private HopcroftKarpGraph getProblem(File in) throws FileNotFoundException {
        problem = new HopcroftKarpParser().parse(new FileInputStream(in));
        return HopcroftKarpGraph.of(problem);
    }

    private String getSolution(File out) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(out);
        } catch (FileNotFoundException e) {
            Assertions.fail("Outputfile was not found.");
        }
        return new Scanner(inputStream).nextLine();
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
}
