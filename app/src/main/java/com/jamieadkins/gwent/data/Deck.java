package com.jamieadkins.gwent.data;

import android.content.Context;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.jamieadkins.gwent.R;

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
    private String author;
    private String faction;
    private String leader;
    private String patch = "v0-8-60-2";
    // Map of card ids to variation ids to card count.
    private Map<String, Integer> cards;

    public Deck() {
        // Required empty constructor for Firebase.
    }

    public Deck(String id, String name, String faction, String author) {
        this.id = id;
        this.name = name;
        this.faction = faction;
        this.author = author;
        this.openToPublic = false;
    }

    public Deck(String id, String faction, String author) {
        this(id, id, faction, author);
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }

    public String getPatch() {
        return patch;
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
        result.put("author", author);
        result.put("faction", faction);
        result.put("cards", cards);
        result.put("leader", leader);
        result.put("patch", patch);
        result.put("openToPublic", openToPublic);

        return result;
    }
}
