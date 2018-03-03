package com.jamieadkins.gwent.data.deck;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.base.BaseDisposableSingle;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.data.repository.card.CardRepository;
import com.jamieadkins.gwent.model.CardColour;
import com.jamieadkins.gwent.model.GwentCard;

import java.util.*;
import java.util.Collection;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;

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
    private GwentCard leader;
    private String leaderId;
    private String patch = "v0-8-60-2";
    // Map of card ids to card count.
    private Map<String, Integer> cardCount;
    private Map<String, GwentCard> cards;

    private boolean deleted = false;

    public Deck() {
        // Required empty constructor for Firebase.
        this.cardCount = new HashMap<>();
    }

    public Deck(String id, String name, String factionId, String leader,
                String author) {
        this();
        this.id = id;
        this.name = name;
        this.factionId = factionId;
        this.leaderId = leader;
        this.author = author;
        this.publicDeck = false;
        this.deleted = false;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLeaderId() {
        return leaderId;
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getCardCount() {
        return cardCount;
    }

    public boolean isPublicDeck() {
        return publicDeck;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
        result.put("cardCount", cardCount);
        result.put("leaderId", leaderId);
        result.put("patch", patch);
        result.put("publicDeck", publicDeck);
        result.put("deleted", deleted);

        return result;
    }

    @Exclude
    public Map<String, GwentCard> getCards() {
        if (cards != null && cards.keySet().size() == cardCount.keySet().size()) {
            return cards;
        } else {
            throw new RuntimeException("Deck has not been evaluated!");
        }
    }

    @Exclude
    public GwentCard getLeader() {
        if (leader != null) {
            return leader;
        } else {
            throw new RuntimeException("Deck has not been evaluated!");
        }
    }

    @Exclude
    public Completable evaluateDeck(final CardRepository cardRepository) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                return new Completable() {
                    @Override
                    protected void subscribeActual(final CompletableObserver emitter) {
                        if (cards == null) {
                            cards = new HashMap<String, GwentCard>();
                        }

                        cardRepository.getCard(leaderId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(
                                new BaseDisposableSingle<GwentCard>() {
                                    @Override
                                    public void onSuccess(GwentCard cardDetails) {
                                        leader = cardDetails;
                                        if (cards.keySet().size() == cardCount.keySet().size()) {
                                            emitter.onComplete();
                                        }
                                    }
                                });

                        if (cardCount.keySet().size() == 0) {
                            return;
                        }

                        ArrayList<String> cardIds = new ArrayList<>();
                        for (String cardId : cardCount.keySet()) {
                            cardIds.add(cardId);
                        }

                        cardRepository.getCards(cardIds)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(
                                new BaseDisposableSingle<Collection<GwentCard>>() {
                                    @Override
                                    public void onSuccess(Collection<GwentCard> result) {
                                        for (GwentCard card : result) {
                                            cards.put(card.getId(), card);
                                        }

                                        if (leader != null) {
                                            emitter.onComplete();
                                        }
                                    }
                                });
                    }
                };
            }
        });
    }

    @Exclude
    public int getStrengthForPosition(String position) {
        int strength = 0;
        return strength;
    }

    @Exclude
    public int getTotalStrength() {
        int strength = 0;
        for (String cardId : getCards().keySet()) {
            GwentCard card = getCards().get(cardId);
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
    public Single<Integer> getSilverCardCount() {
        return getCardCount(CardColour.SILVER);
    }

    @Exclude
    public Single<Integer> getGoldCardCount() {
        return getCardCount(CardColour.GOLD);
    }

    @Exclude
    private Single<Integer> getCardCount(final CardColour colour) {
        return Single.defer(new Callable<SingleSource<? extends Integer>>() {
            @Override
            public SingleSource<? extends Integer> call() throws Exception {
                return new Single<Integer>() {
                    @Override
                    protected void subscribeActual(SingleObserver<? super Integer> observer) {
                        int count = 0;
                        for (String cardId : cardCount.keySet()) {
                            if (getCards().get(cardId).getColour().equals(colour)) {
                                count += cardCount.get(cardId);
                            }
                        }
                        observer.onSuccess(count);
                    }
                };
            }
        });
    }

    @Exclude
    @Override
    public int getItemType() {
        return GwentRecyclerViewAdapter.TYPE_DECK;
    }

    @Exclude
    @Override
    public boolean areContentsTheSame(RecyclerViewItem other) {
        return other instanceof Deck && leaderId.equals(((Deck) other).getLeaderId()) &&
                getTotalCardCount() == ((Deck) other).getTotalCardCount() &&
                name.equals(((Deck) other).name);
    }
}
