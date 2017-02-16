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
    private String author;
    private String faction;
    private CardDetails leader;
    private String patch = "v0-8-60-2";
    // Map of card ids to card count.
    private Map<String, Integer> cardCount;
    private Map<String, CardDetails> cards;

    public Deck() {
        // Required empty constructor for Firebase.
        this.cards = new HashMap<>();
        this.cardCount = new HashMap<>();
    }

    public Deck(String id, String name, String faction, String author, String patch) {
        this.id = id;
        this.name = name;
        this.faction = faction;
        this.author = author;
        this.patch = patch;
        this.openToPublic = false;
    }

    public void setLeader(CardDetails leader) {
        this.leader = leader;
    }

    public CardDetails getLeader() {
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

    public Map<String, CardDetails> getCards() {
        return cards;
    }

    public Map<String, Integer> getCardCount() {
        return cardCount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("author", author);
        result.put("faction", faction);
        result.put("cards", cards);
        result.put("cardCount", cardCount);
        result.put("leader", leader);
        result.put("patch", patch);
        result.put("openToPublic", openToPublic);

        return result;
    }
}
