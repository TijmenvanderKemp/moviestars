package com.tijmen.part1;

import java.io.InputStream;
import java.util.Scanner;

public class ReaderImpl implements Reader {
    final Scanner scanner;

    public ReaderImpl() {
        this.scanner = new Scanner(System.in);
    }

    public ReaderImpl(InputStream inputMethod) {
        this.scanner = new Scanner(inputMethod);
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }
}
