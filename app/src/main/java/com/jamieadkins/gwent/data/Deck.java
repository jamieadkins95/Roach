package com.jamieadkins.gwent.data;

import com.jamieadkins.jgaw.Faction;

/**
 * Class that models what a deck is.
 */

public class Deck {
    private String id;
    private String name = "Some Deck";
    private String faction = Faction.SKELLIGE;

    public Deck() {
        // Required empty constructor for Firebase.
    }

    public Deck(String name, String faction) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.faction = faction;
    }

    public Deck(String faction) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = id;
        this.faction = faction;
    }

    public String getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public String getId() {
        return id;
    }
}
