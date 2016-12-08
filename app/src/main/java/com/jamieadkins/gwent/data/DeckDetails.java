package com.jamieadkins.gwent.data;

import java.util.List;

/**
 * Class that models what a deck is with more detail than an ordinary deck.
 */

public class DeckDetails extends Deck {
    private List<Card> cards;

    public DeckDetails() {
        // Required empty constructor for Firebase.
    }

    public DeckDetails(Deck deck) {
        super(deck.getName(), deck.getFaction());
    }

    public List<Card> getCards() {
        return cards;
    }
}
