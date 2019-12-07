package com.tijmen;

import java.io.InputStream;
import java.util.Scanner;

public class ProblemParser {
    public Problem parse(InputStream inputMethod) {
        Scanner in = new Scanner(inputMethod);

        String s = in.nextLine();
        String[] edgesAndVerticesAndBins = s.split(" ");
        int numberOfEdges = Integer.parseInt(edgesAndVerticesAndBins[0]);
        int numberOfVertices = Integer.parseInt(edgesAndVerticesAndBins[1]);
        int numberOfBins = Integer.parseInt(edgesAndVerticesAndBins[2]);

        return new Problem(null, null, null);
    }
}
