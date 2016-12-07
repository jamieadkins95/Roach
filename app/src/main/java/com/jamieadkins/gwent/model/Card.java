package com.jamieadkins.gwent.model;

import com.orm.SugarRecord;

/**
 * Created by jamiea on 07/12/16.
 */

public class Card extends SugarRecord {
    private String cardId;

    public Card() {
        // Required empty constructor for Sugar ORM.
    }

    public Card(String cardId) {
        this.cardId = cardId;
    }
}
