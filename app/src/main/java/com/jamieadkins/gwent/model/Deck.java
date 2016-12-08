package com.jamieadkins.gwent.model;

import com.jamieadkins.jgaw.Faction;
import java.util.List;

/**
 * Class that models what a deck is.
 */

public class Deck {
    private String name = "Some Deck";
    private Faction faction = Faction.SKELLIGE;

    public Deck() {
        // Required empty constructor for Sugar ORM.
    }

    public Deck(Faction faction) {
        this.faction = faction;
    }

    public String getName() {
        return name;
    }
}
