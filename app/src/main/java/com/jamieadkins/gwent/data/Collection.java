package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models what a deck is.
 */
@IgnoreExtraProperties
public class Collection {
    // Map of CardIds to VariationIds to card count.
    private Map<String, Map<String, Integer>> cards;

    public Collection() {
        cards = new HashMap<>();
    }

    public Map<String, Map<String, Integer>> getCards() {
        return cards;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cards", cards);
        return result;
    }
}
