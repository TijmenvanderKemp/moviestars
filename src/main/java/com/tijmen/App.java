package com.tijmen;

public final class App
{
    private App() {
    }

    public static void main( String[] args )
    {
        ProblemParser parser = new ProblemParser();
        Problem problem = parser.parse(System.in);
        Player victor = new Algorithm(problem).solve();
        System.out.println(victor.getName());
    }
}
