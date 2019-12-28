package com.tijmen;

public enum Player {
    MARK("Mark"),
    VERONIQUE("Veronique");

    private String name;

    Player(String name) {
        this.name = name;
    }

    public static Player parse(String name) {
        return valueOf(name.toUpperCase());
    }

    public Player next() {
        return this == MARK ? VERONIQUE : MARK;
    }

    public String getName() {
        return name;
    }
}
