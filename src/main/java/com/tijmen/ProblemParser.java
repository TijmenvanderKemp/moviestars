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

        Graph graph = new Graph(numberOfVertices);

        for (int i = 0; i < numberOfEdges; i++) {
            parseEdge(in, graph);
        }

        graph.buildAdjacencyLists();

        return new Problem(graph, numberOfBins);
    }

    private void parseEdge(Scanner in, Graph graph) {
        String edgeAsString = in.nextLine();
        String[] verticesFromAndTo = edgeAsString.split(" ");
        int vertexFrom = Integer.parseInt(verticesFromAndTo[0]);
        int vertexTo = Integer.parseInt(verticesFromAndTo[1]);

        graph.addEdge(vertexFrom, vertexTo);
    }
}
