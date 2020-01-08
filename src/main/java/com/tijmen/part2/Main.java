package com.tijmen.part2;

public class Main {
    public static void main(String[] args) {
        new CompetitionRunner(new ReaderImpl(), new WriterImpl()).start();
    }
}
