package com.tijmen.part2;

public enum Player {
    MARK("Mark"),
    VERONIQUE("Veronique");

    private final String name;

    Player(String name) {
        this.name = name;
    }

    public static Player parse(String name) {
        return valueOf(name.toUpperCase());
    }

    public String getName() {
        return name;
    }
}
