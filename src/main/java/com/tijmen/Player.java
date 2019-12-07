package com.tijmen;

public enum Player {
    MARK,
    VERONIQUE;

    private Player next() {
        return this == MARK ? VERONIQUE : MARK;
    }
}
