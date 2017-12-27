package com.jamieadkins.gwent.data.collection;

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
    @Override
    public boolean equals(Object obj) {
        boolean equal = true;

        if (!(obj instanceof Collection)) {
            return false;
        }

        Collection other = (Collection) obj;

        try {
            for (String cardId : cards.keySet()) {
                for (String variationId : cards.get(cardId).keySet()) {
                    if (!cards.get(cardId).get(variationId).equals(other.getCards().get(cardId).get(variationId))) {
                        equal = false;
                    }
                }
            }

            for (String cardId : other.getCards().keySet()) {
                for (String variationId : other.getCards().get(cardId).keySet()) {
                    if (!cards.get(cardId).get(variationId).equals(other.getCards().get(cardId).get(variationId))) {
                        equal = false;
                    }
                }
            }
        } catch (Exception exception) {
            equal = false;
        }

        return equal;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cards", cards);
        return result;
    }
}
