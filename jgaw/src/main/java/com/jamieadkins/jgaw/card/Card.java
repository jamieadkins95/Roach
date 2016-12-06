package com.jamieadkins.jgaw.card;

import java.util.List;

/**
 * Object representing a Gwent card.
 */

public class Card extends BaseApiResult {
    private Faction faction;
    private Rarity rarity;
    private Type type;
    private String text;
    private String id;
    private Artwork artwork;
    private List<String> rows;
    private List<Subtype> subtypes;

    @Override
    public String toString() {
        return name;
    }


}
