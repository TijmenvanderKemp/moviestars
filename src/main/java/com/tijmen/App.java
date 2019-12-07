package com.tijmen;

import java.util.HashMap;

public final class App
{
    private App() {
    }

    public static void main( String[] args )
    {
        ProblemParser parser = new ProblemParser();
        Problem problem = parser.parse(System.in);
    }
}
