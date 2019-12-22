package com.tijmen;

import com.tijmen.hopcroftkarp.HopcroftKarpAlgorithm;
import com.tijmen.hopcroftkarp.HopcroftKarpGraph;
import com.tijmen.hopcroftkarp.HopcroftKarpParser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class AllFilesTestHopcroftKarpAlgorithm {
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
            HopcroftKarpAlgorithm hopcroftKarpAlgorithm = new HopcroftKarpAlgorithm(getProblem(inAndOut));
            String solution = getSolution(inAndOut);
            testSolve(hopcroftKarpAlgorithm, solution);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void testSolve(HopcroftKarpAlgorithm hopcroftKarpAlgorithm, String solution) {
        Player victor = hopcroftKarpAlgorithm.solve();
        assertThat(victor.getName()).isEqualTo(solution);
    }

    private HopcroftKarpGraph getProblem(Pair<File, File> inAndOut) throws FileNotFoundException {
        return new HopcroftKarpParser().parse(new FileInputStream(inAndOut.getLeft()));
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
}
