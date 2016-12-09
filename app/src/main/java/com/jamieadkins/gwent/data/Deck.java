package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models what a deck is.
 */
@IgnoreExtraProperties
public class Deck {
    private boolean openToPublic;
    private String id;
    private String name;
    private String faction;
    private Map<String, Integer> cards;

    public Deck() {
        // Required empty constructor for Firebase.
    }

    public Deck(String id, String name, String faction) {
        this.id = id;
        this.name = name;
        this.faction = faction;
        this.openToPublic = false;
    }

    public Deck(String id, String faction) {
        this(id, id, faction);
    }

    public String getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public String getId() {
        return id;
    }

    public void initialiseCardMap() {
        this.cards = new HashMap<>();
    }

    public Map<String, Integer> getCards() {
        return cards;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("faction", faction);
        result.put("cards", cards);
        result.put("openToPublic", openToPublic);

        return result;
    }
}
