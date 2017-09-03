package com.jamieadkins.gwent.data.interactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.base.BaseCompletableObserver;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.data.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;
import kotlin.NotImplementedError;

import static com.jamieadkins.gwent.data.Deck.MAX_CARD_COUNT;
import static com.jamieadkins.gwent.data.Deck.MAX_EACH_BRONZE;
import static com.jamieadkins.gwent.data.Deck.MAX_EACH_GOLD;
import static com.jamieadkins.gwent.data.Deck.MAX_EACH_SILVER;

/**
 * Deals with firebase.
 */

public class DecksInteractorFirebase implements DecksInteractor {
    private static final String PUBLIC_DECKS_PATH = "public-decks/";

    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private Query mDecksQuery;
    private Query mDeckQuery;
    private Query mCardCountQuery;
    private final DatabaseReference mPublicReference;
    private DatabaseReference mUserReference;

    private CardsInteractor cardsInteractor;

    private List<ChildEventListener> mDeckListListeners = new ArrayList<>();
    private List<ValueEventListener> mDeckDetailListeners = new ArrayList<>();
    private List<ChildEventListener> mCardCountListeners = new ArrayList<>();

    public DecksInteractorFirebase() {
        mPublicReference = mDatabase.getReference(PUBLIC_DECKS_PATH);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mUserReference = mDatabase.getReference("users/" + userId + "/decks/");
            mUserReference.keepSynced(true);
        }
    }

    private Deck checkLegacy(DataSnapshot deckSnapshot) {
        Deck deck = deckSnapshot.getValue(Deck.class);

        if (!deckSnapshot.hasChild("leaderId") && deckSnapshot.hasChild("leader")) {
            deck.setLeaderId(deckSnapshot.child("leader").child("ingameId").getValue(String.class));
        }

        return deck;
    }

    public void setCardsInteractor(CardsInteractor cardsInteractor) {
        this.cardsInteractor = cardsInteractor;
    }

    private Observable<RxDatabaseEvent<Deck>> getDecks(Query query) {
        mDecksQuery = query;
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        ValueEventListener initialCountListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emitter.onNext(RxDatabaseEvent.INITIAL_LOAD_COMPLETE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        ChildEventListener listener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot deckSnapshot, String s) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(cardsInteractor)
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        RxDatabaseEvent.EventType eventType = RxDatabaseEvent.EventType.ADDED;
                                                        if (deck.isDeleted()) {
                                                            eventType = RxDatabaseEvent.EventType.REMOVED;
                                                        }

                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        eventType));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onChildChanged(DataSnapshot deckSnapshot, String s) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(cardsInteractor)
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        RxDatabaseEvent.EventType eventType = RxDatabaseEvent.EventType.CHANGED;
                                                        if (deck.isDeleted()) {
                                                            eventType = RxDatabaseEvent.EventType.REMOVED;
                                                        }

                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        eventType));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot deckSnapshot) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(cardsInteractor)
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        RxDatabaseEvent.EventType.REMOVED));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onChildMoved(DataSnapshot deckSnapshot, String s) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(cardsInteractor)
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        RxDatabaseEvent.EventType.MOVED));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDecksQuery.addChildEventListener(listener);
                        mDeckListListeners.add(listener);
                        mDecksQuery.addListenerForSingleValueEvent(initialCountListener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Integer>> subscribeToCardCountUpdates(String deckId) {
        mCardCountQuery = mUserReference.child(deckId).child("cardCount");
        mCardCountQuery.keepSynced(true);
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Integer>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Integer>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Integer>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Integer>> emitter) throws Exception {
                        ChildEventListener listener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                int count = dataSnapshot.getValue(Integer.class);
                                RxDatabaseEvent.EventType type = RxDatabaseEvent.EventType.ADDED;
                                if (count == 0) {
                                    type = RxDatabaseEvent.EventType.REMOVED;
                                }

                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        type));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                int count = dataSnapshot.getValue(Integer.class);
                                RxDatabaseEvent.EventType type = RxDatabaseEvent.EventType.CHANGED;
                                if (count == 0) {
                                    type = RxDatabaseEvent.EventType.REMOVED;
                                }

                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        type));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                int count = dataSnapshot.getValue(Integer.class);
                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        RxDatabaseEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                int count = dataSnapshot.getValue(Integer.class);
                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        RxDatabaseEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mCardCountQuery.addChildEventListener(listener);
                        mCardCountListeners.add(listener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getFeaturedDecks() {
        Query query = mPublicReference.child("decks").orderByChild("week");
        return getDecks(query);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getUserDecks() {
        Query query = mUserReference.orderByChild("name");
        return getDecks(query);
    }

    @Override
    public Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek() {
        mDeckQuery = mPublicReference.child("deck-of-the-week");
        return Single.defer(new Callable<SingleSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public SingleSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Single.create(new SingleOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final SingleEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        ValueEventListener listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Deck deck = checkLegacy(dataSnapshot);

                                if (deck == null) {
                                    emitter.onError(new Throwable("Deck doesn't exist"));
                                    return;
                                }

                                emitter.onSuccess(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                deck,
                                                RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDeckQuery.addListenerForSingleValueEvent(listener);
                        mDeckDetailListeners.add(listener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(final String deckId, boolean isPublicDeck) {
        // Return unevaluated deck by default.
        return getDeck(deckId, isPublicDeck, false);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(final String deckId, boolean isPublicDeck,
                                                     final boolean evaluate) {
        if (isPublicDeck) {
            mDeckQuery = mPublicReference.child("decks").child(deckId);
        } else {
            mDeckQuery = mUserReference.child(deckId);
        }
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        ValueEventListener listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Deck deck = checkLegacy(dataSnapshot);

                                if (deck == null) {
                                    emitter.onError(new Throwable("Deck doesn't exist"));
                                    emitter.onComplete();
                                    return;
                                }

                                if (evaluate) {
                                    deck.evaluateDeck(cardsInteractor)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                    new BaseCompletableObserver() {
                                                        @Override
                                                        public void onComplete() {
                                                            emitter.onNext(
                                                                    new RxDatabaseEvent<Deck>(
                                                                            deck.getId(),
                                                                            deck,
                                                                            RxDatabaseEvent.EventType.ADDED));
                                                        }
                                                    }
                                            );
                                } else {
                                    emitter.onNext(
                                            new RxDatabaseEvent<Deck>(
                                                    deck.getId(),
                                                    deck,
                                                    RxDatabaseEvent.EventType.ADDED));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDeckQuery.addValueEventListener(listener);
                        mDeckDetailListeners.add(listener);
                    }
                });
            }
        });
    }

    @Override
    public void stopData() {
        if (mDecksQuery != null && mDeckListListeners != null) {
            for (ChildEventListener listener : mDeckListListeners) {
                mDecksQuery.removeEventListener(listener);
            }
            mDeckListListeners.clear();
        }
        if (mDeckQuery != null && mDeckDetailListeners != null) {
            for (ValueEventListener listener : mDeckDetailListeners) {
                mDeckQuery.removeEventListener(listener);
            }
            mDeckDetailListeners.clear();
        }
        if (mCardCountQuery != null && mCardCountListeners != null) {
            for (ChildEventListener listener : mCardCountListeners) {
                mCardCountQuery.removeEventListener(listener);
            }
            mCardCountListeners.clear();
        }
    }

    @Override
    public String createNewDeck(String name, String faction) {
        String key = mUserReference.push().getKey();
        String author = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String leaderId = null;
        switch (faction) {
            case Faction.MONSTERS_ID:
                leaderId = "131101";
                break;
            case Faction.NILFGAARD_ID:
                leaderId = "200162";
                break;
            case Faction.NORTHERN_REALMS_ID:
                leaderId = "200168";
                break;
            case Faction.SKELLIGE_ID:
                leaderId = "200160";
                break;
            case Faction.SCOIATAEL_ID:
                leaderId = "200165";
                break;
        }
        Deck deck = new Deck(key, name, faction, leaderId, author);
        Map<String, Object> deckValues = deck.toMap();

        Map<String, Object> firebaseUpdates = new HashMap<>();
        firebaseUpdates.put(key, deckValues);

        mUserReference.updateChildren(firebaseUpdates);

        return key;
    }

    @Override
    public void publishDeck(String deckId) {
        throw new NotImplementedError();
    }

    @Override
    public void setLeader(String deckId, final String leaderId) {
        DatabaseReference deckReference = mUserReference.child(deckId).child("leaderId");

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // Set value and report transaction success.
                mutableData.setValue(leaderId);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }

    @Override
    public void renameDeck(final String deckId, final String newName) {
        DatabaseReference deckReference = mUserReference.child(deckId).child("name");

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // Set value and report transaction success.
                mutableData.setValue(newName);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }

    @Override
    public void deleteDeck(final String deckId) {
        DatabaseReference deckReference = mUserReference.child(deckId).child("deleted");

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // Set value and report transaction success.
                mutableData.setValue(true);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }

    @Override
    public void addCardToDeck(final String deckId, final CardDetails card) {
        DatabaseReference deckReference = mUserReference.child(deckId).child("cardCount");

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Map<String, Long> cards = (Map<String, Long>) mutableData.getValue();

                if (cards == null) {
                    cards = new HashMap<String, Long>();
                }

                if (!canAddCard(cards, card)) {
                    return Transaction.success(mutableData);
                }

                if (cards.containsKey(card.getIngameId())) {
                    // If the user already has at least one of these cards in their deck.
                    long currentCardCount = cards.get(card.getIngameId());
                    cards.put(card.getIngameId(), currentCardCount + 1);
                } else {
                    // Else add one card to the deck.
                    cards.put(card.getIngameId(), 1L);
                }

                // Set value and report transaction success.
                mutableData.setValue(cards);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }

    public boolean canAddCard(Map<String, Long> cards, CardDetails cardDetails) {
        int count = 0;
        for (String cardId : cards.keySet()) {
            count += cards.get(cardId);
        }

        if (count >= MAX_CARD_COUNT) {
            return false;
        }

        if (cards.containsKey(cardDetails.getIngameId())) {
            // If the user already has at least one of these cards in their deck.
            long currentCardCount = cards.get(cardDetails.getIngameId());
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

    @Override
    public void removeCardFromDeck(final String deckId, final CardDetails card) {
        DatabaseReference deckReference = mUserReference.child(deckId).child("cardCount");

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Map<String, Long> cards = (Map<String, Long>) mutableData.getValue();

                if (cards.containsKey(card.getIngameId())) {
                    // If the user already has at least one of these cards in their deck.
                    long currentCardCount = cards.get(card.getIngameId());
                    long newCount = currentCardCount - 1;

                    if (newCount == 0) {
                        cards.remove(card.getIngameId());
                    } else {
                        cards.put(card.getIngameId(), newCount);
                    }
                } else {
                    // This deck doesn't have that card in it.
                }

                // Set value and report transaction success.
                mutableData.setValue(cards);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }
}
