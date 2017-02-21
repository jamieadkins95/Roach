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
    private boolean publicDeck;
    private String id;
    private String name;
    private String author;
    private String factionId;
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

    public Deck(String id, String name, String factionId, CardDetails leader,
                String author, String patch) {
        this.id = id;
        this.name = name;
        this.factionId = factionId;
        this.leader = leader;
        this.author = author;
        this.patch = patch;
        this.publicDeck = false;
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

    public String getFactionId() {
        return factionId;
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

    public boolean isPublicDeck() {
        return publicDeck;
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Deck && id.equals(((Deck) obj).getId());
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("author", author);
        result.put("factionId", factionId);
        result.put("cards", cards);
        result.put("cardCount", cardCount);
        result.put("leader", leader);
        result.put("patch", patch);
        result.put("publicDeck", publicDeck);

        return result;
    }

    @Exclude
    public int getStrengthForPosition(String position) {
        int strength = 0;
        for (String cardId : cards.keySet()) {
            CardDetails card = cards.get(cardId);
            if (card.getLane().contains(position)) {
                strength += card.getStrength();
            }
        }
        return strength;
    }

    @Exclude
    public int getTotalStrength() {
        int strength = 0;
        for (String cardId : cards.keySet()) {
            CardDetails card = cards.get(cardId);
            strength += card.getStrength();
        }
        return strength;
    }

    @Exclude
    public int getTotalCardCount() {
        int count = 0;
        for (String cardId : cardCount.keySet()) {
            count += cardCount.get(cardId);
        }
        return count;
    }
}
