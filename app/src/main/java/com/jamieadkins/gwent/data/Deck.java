package com.jamieadkins.gwent.data;

import com.jamieadkins.jgaw.Faction;

/**
 * Class that models what a deck is.
 */

public class Deck {
    private String name = "Some Deck";
    private Faction faction = Faction.SKELLIGE;

    public Deck() {
        // Required empty constructor for Firebase.
    }

    public Deck(Faction faction) {
        this.faction = faction;
    }

    public String getName() {
        return name;
    }
}
