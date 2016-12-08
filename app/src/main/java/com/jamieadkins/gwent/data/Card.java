package com.jamieadkins.gwent.data;

/**
 * Created by jamiea on 07/12/16.
 */

public class Card {
    private String cardId;

    public Card() {
        // Required empty constructor for Firebase
    }

    public Card(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}
