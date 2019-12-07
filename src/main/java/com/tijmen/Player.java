package com.tijmen;

public enum Player {
    MARK("Mark"),
    VERONIQUE("Veronique");

    String name;

    private Player(String name) {
        this.name = name;
    }

    public Player next() {
        return this == MARK ? VERONIQUE : MARK;
    }

    public String getName() {
        return name;
    }
}
