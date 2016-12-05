package com.jamieadkins.jgaw;

/**
 * Object representing a Gwent card.
 */

public class CardStubResult {
    private String name;
    private String href;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        final int lastSlash = href.lastIndexOf("/");
        return href.substring(lastSlash);
    }
}
