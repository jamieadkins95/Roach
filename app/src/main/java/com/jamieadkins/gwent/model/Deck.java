package com.jamieadkins.gwent.model;

import com.jamieadkins.jgaw.Faction;
import com.jamieadkins.jgaw.card.CardDetails;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that models what a deck is.
 */

public class Deck extends SugarRecord {
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

    List<Card> getCards() {
        return DeckCard.find(Card.class, "deck = ?", String.valueOf(getId()));
    }
}
