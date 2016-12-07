package com.jamieadkins.gwent.model;

import com.orm.SugarRecord;

/**
 * Class that models what a deck is.
 */

public class DeckCard extends SugarRecord {
    private Deck deck;
    private Card card;

    public DeckCard() {
        // Required empty constructor for Sugar ORM.
    }
}
