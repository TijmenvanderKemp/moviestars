package com.tijmen;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private final Set<Vertex> vertices;
    private final Set<Edge> edges;
    private final Map<Vertex, Set<Vertex>> adjacencyLists = new HashMap<>();

    public Graph(int numberOfVertices) {
        vertices = new HashSet<>();
        edges = new HashSet<>();
        for (int i = 1; i <= numberOfVertices; i++) {
            vertices.add(new Vertex(i));
        }
    }

    public Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        buildAdjacencyLists();
    }

    public void addEdge(int v1, int v2) {
        Edge newEdge = new Edge(getVertex(v1), getVertex(v2));
        edges.add(newEdge);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void buildAdjacencyLists() {
        for (Vertex vertex : vertices) {
            adjacencyLists.put(vertex, new HashSet<>());
        }
        for(Edge edge : edges) {
            Vertex v1 = edge.getV1();
            Vertex v2 = edge.getV2();
            Set<Vertex> verticesV1 = adjacencyLists.get(v1);
            verticesV1.add(v2);
            Set<Vertex> verticesV2 = adjacencyLists.get(v2);
            verticesV2.add(v1);
        }
    }

    public Vertex getVertex(int id) {
        return vertices.stream()
                .filter(v -> id == v.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Vertex with id %d not found: ", id)));
    }

    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    public Map<Vertex, Set<Vertex>> getAdjacencyLists() {
        return Collections.unmodifiableMap(adjacencyLists);
    }

    public int numberOfVertices() {
        return vertices.size();
    }

    public int numberOfEdges() {
        return edges.size();
    }

    public int getGrade(Vertex v) {
        return adjacencyLists.get(v).size();
    }

    public Vertex getVertexWithMinimalGrade() {
        return adjacencyLists.entrySet().stream()
                .min(Comparator.comparing(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey)
                .orElseThrow(NullPointerException::new);
    }

    public Optional<Vertex> findVertexWithGrade(int grade) {
        return getVerticesWithGrade(grade)
                .stream()
                .findAny();
    }

    public Set<Vertex> getVerticesWithGrade(int grade) {
        return adjacencyLists.entrySet().stream()
                .filter(entry -> entry.getValue().size() == grade)
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public Set<Vertex> getVertexAndNeighbours(Vertex v) {
        Set<Vertex> vertexAndNeighbours = new HashSet<>();
        vertexAndNeighbours.add(v);
        vertexAndNeighbours.addAll(adjacencyLists.get(v));
        return vertexAndNeighbours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return vertices.equals(graph.getVertices()) && edges.equals(graph.getEdges());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    @Override
    public String toString() {
        return "\nGraph{" +
                "\nvertices=" + vertices +
                ", \nedges=" + edges +
                '}';
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }
}
