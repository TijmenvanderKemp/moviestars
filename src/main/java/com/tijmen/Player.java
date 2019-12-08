package com.tijmen;

public enum Player {
    MARK("Mark"),
    VERONIQUE("Veronique");

    private String name;

    Player(String name) {
        this.name = name;
    }

    public Player next() {
        return this == MARK ? VERONIQUE : MARK;
    }

    public String getName() {
        return name;
    }
}
