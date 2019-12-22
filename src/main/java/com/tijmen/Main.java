package com.tijmen;

public final class Main
{
    private Main() {
    }

    public static void main( String[] args )
    {
        GraphParser parser = new GraphParser();
        Graph graph = parser.parse(System.in);
        Player victor = new Algorithm(graph).solve();
        System.out.println(victor.getName());
    }
}
