package com.jamieadkins.gwent.model;

/**
 * Created by jamiea on 07/12/16.
 */

public class Card {
    private String cardId;

    public Card() {
        // Required empty constructor for Sugar ORM.
    }

    public Card(String cardId) {
        this.cardId = cardId;
    }
}
