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
    private Map<String, Integer> cards;

    public Collection() {
        // Required empty constructor for Firebase.
    }

    public Map<String, Integer> getCards() {
        return cards;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cards", cards);
        return result;
    }
}
