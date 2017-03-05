package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models what a deck is.
 */
@IgnoreExtraProperties
public class Deck implements RecyclerViewItem {
    public static final int MAX_CARD_COUNT = 40;
    public static final int MIN_CARD_COUNT = 25;
    public static final int MAX_SILVER_COUNT = 6;
    public static final int MAX_GOLD_COUNT = 4;

    public static final int MAX_EACH_BRONZE = 3;
    public static final int MAX_EACH_SILVER = 1;
    public static final int MAX_EACH_GOLD = 1;

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

    public void setName(String name) {
        this.name = name;
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
                strength += card.getStrength() * cardCount.get(cardId);
            }
        }
        return strength;
    }

    @Exclude
    public int getTotalStrength() {
        int strength = 0;
        for (String cardId : cards.keySet()) {
            CardDetails card = cards.get(cardId);
            strength += card.getStrength() * cardCount.get(cardId);
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

    @Exclude
    public int getSilverCardCount() {
        return getCardCount(Type.SILVER_ID);
    }

    @Exclude
    public int getGoldCardCount() {
        return getCardCount(Type.GOLD_ID);
    }

    @Exclude
    private int getCardCount(String type) {
        int count = 0;
        for (String cardId : cardCount.keySet()) {
            if (cards.get(cardId).getType().equals(type)) {
                count += cardCount.get(cardId);
            }
        }
        return count;
    }

    @Exclude
    public boolean canAddCard(CardDetails cardDetails) {
        if (getTotalCardCount() >= MAX_CARD_COUNT) {
            return false;
        }

        if (getCardCount().containsKey(cardDetails.getIngameId())) {
            // If the user already has at least one of these cards in their deck.
            int currentCardCount = getCardCount().get(cardDetails.getIngameId());
            switch (cardDetails.getType()) {
                case Type.BRONZE_ID:
                    return currentCardCount < MAX_EACH_BRONZE;
                case Type.SILVER_ID:
                    return currentCardCount < MAX_EACH_SILVER;
                case Type.GOLD_ID:
                    return currentCardCount < MAX_EACH_GOLD;
                default:
                    return false;
            }
        } else {
            // Deck doesn't contain this card yet, can add as long as the card isn't a leader card.
            return !cardDetails.getType().equals(Type.LEADER_ID);
        }

    }

    @Exclude
    @Override
    public int getItemType() {
        return GwentRecyclerViewAdapter.TYPE_DECK;
    }
}
