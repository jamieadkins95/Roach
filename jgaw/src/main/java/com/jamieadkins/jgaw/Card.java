package com.jamieadkins.jgaw;

/**
 * Object representing a Gwent card.
 */

public class Card {
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
